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
 *
 * Lettie's picks are deterministic and branching-aware (QWERTY anti-jam): she prefers
 * moves that leave the child many replies — never a coin flip among equals.
 */
class GameSession(
    private val repo: WordRepository,
    val round: Round,
    val activeCategories: List<Category>,
    val difficulty: Difficulty,
    private val lettieStarts: Boolean = true,
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
            requiredLetter = bestStartLetter()
            whoseTurn = Speaker.CHILD
            null
        }
    }

    /** Reset and start a fresh match (Play again). */
    fun restart(): Word? {
        hasBegun = false
        return start()
    }

    /** Prefer the letter with the most Easy starters so the child has room to play. */
    private fun bestStartLetter(): Char {
        val pool = repo.all.filter { it.category in activeCategories }
        val grouped = pool.groupBy { it.firstLetter.uppercaseChar() }
        return grouped.entries
            .maxWithOrNull(
                compareBy<Map.Entry<Char, List<Word>>>(
                    { it.value.count { word -> word.difficulty == Difficulty.EASY } },
                    { it.value.size },
                ).thenBy { it.key },
            )
            ?.key
            ?: 'A'
    }

    private fun pickLettieOpener(): Word? {
        val pool = repo.all.filter { it.category in activeCategories && it.difficulty == Difficulty.EASY }
            .ifEmpty { repo.all.filter { it.category in activeCategories } }
        val candidates = if (difficulty != Difficulty.HARD) {
            pool.filter { hasReplyFor(it) }.ifEmpty { pool }
        } else {
            pool
        }
        return pickBest(candidates)
    }

    private fun play(word: Word, speaker: Speaker) {
        chain.add(ChainEntry(word, speaker))
        usedIds.add(word.id)
        requiredLetter = word.lastLetter
        if (speaker == Speaker.CHILD) childWordCount++
        whoseTurn = if (speaker == Speaker.CHILD) Speaker.LETTIE else Speaker.CHILD
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

        val chosen = chooseLettieWord(candidates)
        // Easy: concede only when every move leaves the child nearly stuck.
        if (difficulty == Difficulty.EASY &&
            chain.size >= 4 &&
            replyCountAfter(chosen) < easyConcedeBranchThreshold
        ) {
            status = GameStatus.CHILD_WON
            return null
        }

        play(chosen, Speaker.LETTIE)
        return chosen
    }

    private val easyConcedeBranchThreshold = 2

    private fun chooseLettieWord(candidates: List<Word>): Word {
        val preferred = when (difficulty) {
            Difficulty.EASY -> candidates.filter { it.difficulty == Difficulty.EASY }
            Difficulty.MEDIUM -> candidates.filter { it.difficulty != Difficulty.HARD }
            Difficulty.HARD -> candidates.filter { it.difficulty == Difficulty.HARD }
                .ifEmpty { candidates.filter { it.difficulty == Difficulty.MEDIUM } }
        }
        val pool = preferred.ifEmpty { candidates }
        return when (difficulty) {
            Difficulty.HARD -> {
                pickBest(pool, maximizeBranching = false)!!
            }
            else -> {
                val safe = pool.filter { hasReplyFor(it) }.ifEmpty { candidates.filter { hasReplyFor(it) } }
                pickBest(safe.ifEmpty { pool }, maximizeBranching = true)!!
            }
        }
    }

    /**
     * Stable pick: primary key is branching left for the child (max on Easy/Medium,
     * min on Hard), then difficulty rank, then word id.
     */
    private fun pickBest(candidates: List<Word>, maximizeBranching: Boolean = true): Word? {
        if (candidates.isEmpty()) return null
        return candidates.minWith(
            compareBy<Word>(
                { word ->
                    val branch = replyCountAfter(word)
                    if (maximizeBranching) -branch else branch
                },
                { word -> difficultyRank(word.difficulty) },
                { it.id },
            ),
        )
    }

    private fun difficultyRank(d: Difficulty): Int = when (difficulty) {
        Difficulty.EASY -> when (d) {
            Difficulty.EASY -> 0
            Difficulty.MEDIUM -> 1
            Difficulty.HARD -> 2
        }
        Difficulty.MEDIUM -> when (d) {
            Difficulty.MEDIUM -> 0
            Difficulty.EASY -> 1
            Difficulty.HARD -> 2
        }
        Difficulty.HARD -> when (d) {
            Difficulty.HARD -> 0
            Difficulty.MEDIUM -> 1
            Difficulty.EASY -> 2
        }
    }

    private fun replyCountAfter(word: Word): Int {
        val next = word.lastLetter
        return repo.all.count {
            it.category in activeCategories &&
                it.id != word.id &&
                it.id !in usedIds &&
                it.firstLetter.equals(next, ignoreCase = true)
        }
    }

    /** True if the child would have at least one unused word to answer after [word]. */
    private fun hasReplyFor(word: Word): Boolean = replyCountAfter(word) > 0

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
            .minByOrNull { it.id }
}
