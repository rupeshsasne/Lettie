package com.radix2.llm.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.ChainEntry
import com.radix2.llm.domain.Difficulty
import com.radix2.llm.domain.GameStatus
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.Speaker
import com.radix2.llm.domain.Word
import com.radix2.llm.domain.WordMatching
import kotlin.random.Random

/** Outcome of the child attempting a word. */
sealed interface SubmitResult {
    data class Accepted(val word: Word) : SubmitResult
    data class WrongLetter(val word: Word, val required: Char) : SubmitResult
    data class AlreadyUsed(val word: Word) : SubmitResult
    data class NotRecognized(val guesses: List<Word>) : SubmitResult
}

/**
 * Holds all state for one game against Lettie. Backed by Compose snapshot state so
 * the UI recomposes automatically. Rules are enforced strictly (real-contest style).
 */
class GameSession(
    private val repo: WordRepository,
    val round: Round,
    val activeCategories: List<Category>,
    val difficulty: Difficulty,
    private val lettieStarts: Boolean = true,
    private val random: Random = Random.Default,
) {
    val chain = mutableStateListOf<ChainEntry>()

    var requiredLetter by mutableStateOf(' ')
        private set
    var whoseTurn by mutableStateOf(Speaker.CHILD)
        private set
    var status by mutableStateOf(GameStatus.PLAYING)
        private set
    var retriesLeft by mutableStateOf(difficulty.retries)
        private set
    var childWordCount by mutableStateOf(0)
        private set

    /**
     * Remaining ms on the child's turn timer. Lives on the session so leaving to a
     * word detail and coming back resumes the same countdown (does not reset).
     */
    var childTimeLeftMs by mutableStateOf(difficulty.turnSeconds * 1000)
        private set

    /** [chain.size] for which [childTimeLeftMs] was last armed — avoids re-arming on UI resume. */
    private var timerArmedForChainSize: Int = -1

    private val usedIds = mutableSetOf<String>()

    /** True after [start] has been called — survives UI leave/return without resetting. */
    var hasBegun: Boolean = false
        private set

    /** Ids of words already played this game (read-only). */
    val playedIds: Set<String> get() = usedIds

    val lastWord: Word? get() = chain.lastOrNull()?.word

    /**
     * Begin the game once. Safe to call again after navigating away and back —
     * subsequent calls are no-ops so progress is preserved.
     */
    fun start(): Word? {
        if (hasBegun) return lastWord
        hasBegun = true
        chain.clear()
        usedIds.clear()
        status = GameStatus.PLAYING
        retriesLeft = difficulty.retries
        childWordCount = 0
        timerArmedForChainSize = -1
        childTimeLeftMs = difficulty.turnSeconds * 1000
        return if (lettieStarts) {
            val opener = pickLettieOpener()
            if (opener != null) {
                play(opener, Speaker.LETTIE)
            }
            opener
        } else {
            requiredLetter = randomStartLetter()
            whoseTurn = Speaker.CHILD
            null
        }
    }

    /** Reset and start a fresh match (Play again). */
    fun restart(): Word? {
        hasBegun = false
        return start()
    }

    private fun randomStartLetter(): Char {
        val available = repo.all
            .filter { it.category in activeCategories }
            .map { it.firstLetter }
            .distinct()
        return available.randomOrNull(random) ?: 'A'
    }

    private fun pickLettieOpener(): Word? {
        val pool = repo.all.filter { it.category in activeCategories && it.difficulty == Difficulty.EASY }
            .ifEmpty { repo.all.filter { it.category in activeCategories } }
        // Don't open on a dead-end letter (except Hard, where it's fair game).
        if (difficulty != Difficulty.HARD) {
            val safe = pool.filter { hasReplyFor(it) }
            if (safe.isNotEmpty()) return safe.random(random)
        }
        return pool.randomOrNull(random)
    }

    private fun play(word: Word, speaker: Speaker) {
        chain.add(ChainEntry(word, speaker))
        usedIds.add(word.id)
        requiredLetter = word.lastLetter
        if (speaker == Speaker.CHILD) childWordCount++
        whoseTurn = if (speaker == Speaker.CHILD) Speaker.LETTIE else Speaker.CHILD
        // New child turn begins after Lettie (or opener) plays — arm a fresh timer once.
        if (whoseTurn == Speaker.CHILD) {
            armChildTimerIfNeeded()
        }
    }

    /** Arm a full turn timer for the current chain length, only once per turn. */
    fun armChildTimerIfNeeded() {
        if (timerArmedForChainSize == chain.size) return
        timerArmedForChainSize = chain.size
        childTimeLeftMs = difficulty.turnSeconds * 1000
    }

    /** Tick the child timer; no-op if already expired. */
    fun consumeChildTimer(ms: Int) {
        if (childTimeLeftMs <= 0) return
        childTimeLeftMs = (childTimeLeftMs - ms).coerceAtLeast(0)
    }

    /** Process the child's spoken attempt. On success, advances the turn to Lettie. */
    fun submitChild(spoken: String): SubmitResult {
        if (status != GameStatus.PLAYING || whoseTurn != Speaker.CHILD) {
            return SubmitResult.NotRecognized(emptyList())
        }
        val word = repo.findSpoken(spoken, activeCategories)
            ?: return SubmitResult.NotRecognized(repo.guesses(spoken, activeCategories))

        if (word.id in usedIds) return SubmitResult.AlreadyUsed(word)
        if (!word.firstLetter.equals(requiredLetter, ignoreCase = true)) {
            return SubmitResult.WrongLetter(word, requiredLetter)
        }
        play(word, Speaker.CHILD)
        return SubmitResult.Accepted(word)
    }

    /** Confirm a specific guessed word (from confirm chips). */
    fun submitConfirmed(word: Word): SubmitResult {
        if (word.id in usedIds) return SubmitResult.AlreadyUsed(word)
        if (!word.firstLetter.equals(requiredLetter, ignoreCase = true)) {
            return SubmitResult.WrongLetter(word, requiredLetter)
        }
        play(word, Speaker.CHILD)
        return SubmitResult.Accepted(word)
    }

    fun consumeRetry(): Boolean {
        if (retriesLeft > 0) {
            retriesLeft--
            return true
        }
        return false
    }

    /** Lettie takes her turn: returns the word she plays, or null if she concedes (child wins). */
    fun lettieTurn(): Word? {
        if (status != GameStatus.PLAYING) return null
        val candidates = repo.startingWith(requiredLetter, activeCategories, usedIds)
        if (candidates.isEmpty()) {
            status = GameStatus.CHILD_WON
            return null
        }
        // At Easy, Lettie sometimes concedes on purpose so the child can win.
        if (difficulty == Difficulty.EASY && chain.size >= 4 && random.nextFloat() < 0.18f) {
            status = GameStatus.CHILD_WON
            return null
        }
        val chosen = chooseLettieWord(candidates)
        play(chosen, Speaker.LETTIE)
        return chosen
    }

    private fun chooseLettieWord(candidates: List<Word>): Word {
        val preferred = when (difficulty) {
            Difficulty.EASY -> candidates.filter { it.difficulty == Difficulty.EASY }
            Difficulty.MEDIUM -> candidates.filter { it.difficulty != Difficulty.HARD }
            Difficulty.HARD -> candidates.filter { it.difficulty == Difficulty.HARD }
                .ifEmpty { candidates.filter { it.difficulty == Difficulty.MEDIUM } }
        }
        val pool = preferred.ifEmpty { candidates }
        // Fairness: on Easy/Medium, don't hand the child a letter with no possible reply.
        // On Hard, Lettie may exploit dead-end letters (that's the challenge).
        if (difficulty != Difficulty.HARD) {
            // Prefer a safe word at the target difficulty, else any safe word (keeping
            // the game alive outranks the difficulty preference).
            val safe = pool.filter { hasReplyFor(it) }.ifEmpty { candidates.filter { hasReplyFor(it) } }
            if (safe.isNotEmpty()) return safe.random(random)
        }
        return pool.randomOrNull(random) ?: candidates.first()
    }

    /** True if the child would have at least one unused word to answer after [word]. */
    private fun hasReplyFor(word: Word): Boolean {
        val next = word.lastLetter
        return repo.all.any {
            it.category in activeCategories &&
                it.id != word.id &&
                it.id !in usedIds &&
                it.firstLetter.equals(next, ignoreCase = true)
        }
    }

    /** The child ran out of time or gave up — strict rules: Lettie wins. */
    fun childStuck() {
        if (status == GameStatus.PLAYING) status = GameStatus.LETTIE_WON
    }

    fun pause() {
        if (status == GameStatus.PLAYING) status = GameStatus.PAUSED
    }

    fun resume() {
        if (status == GameStatus.PAUSED) status = GameStatus.PLAYING
    }

    /** A word the child *could* have played now (for hints / teach-me moments). */
    fun hintWord(): Word? =
        repo.startingWith(requiredLetter, activeCategories, usedIds)
            .filter { it.difficulty == Difficulty.EASY }
            .ifEmpty { repo.startingWith(requiredLetter, activeCategories, usedIds) }
            .randomOrNull(random)
}
