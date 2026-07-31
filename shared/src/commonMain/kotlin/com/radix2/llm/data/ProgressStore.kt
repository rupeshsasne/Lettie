package com.radix2.llm.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Player progress (games, wins, streaks, best chain, words learned, quiz best), persisted
 * via [KeyValueStore]. All fields are Compose snapshot state so the dashboard updates live.
 *
 * Also tracks how often the child has *said* each word and how often they've *studied*
 * it in flashcards — used to nudge variety without banning favorites.
 */
class ProgressStore(private val store: KeyValueStore) {

    var gamesPlayed by mutableStateOf(store.getInt(K_GAMES, 0)); private set
    var gamesWon by mutableStateOf(store.getInt(K_WON, 0)); private set
    var bestChain by mutableStateOf(store.getInt(K_BEST_CHAIN, 0)); private set
    var currentStreak by mutableStateOf(store.getInt(K_STREAK, 0)); private set
    var bestStreak by mutableStateOf(store.getInt(K_BEST_STREAK, 0)); private set
    var quizBest by mutableStateOf(store.getInt(K_QUIZ_BEST, 0)); private set
    var wordsLearned by mutableStateOf(loadLearned()); private set

    private var playCounts by mutableStateOf(loadCounts(K_PLAY_COUNTS))
    private var studyCounts by mutableStateOf(loadCounts(K_STUDY_COUNTS))
    private var lastPlayedDay = store.getLong(K_LAST_DAY, -1L)

    val wordsLearnedCount: Int get() = wordsLearned.size

    /** Times the child successfully played this word in a game. */
    fun playCount(id: String): Int = playCounts[id] ?: 0

    /** Combined exposure (spoken + flashcards) — lower means “fresher” for the child. */
    fun exposure(id: String): Int = playCount(id) + (studyCounts[id] ?: 0)

    /**
     * Record that the child just said [id] correctly.
     * @return true if this is the first time they've played it.
     */
    fun noteChildPlayed(id: String): Boolean {
        val firstTime = id !in wordsLearned
        playCounts = playCounts + (id to playCount(id) + 1)
        store.putString(K_PLAY_COUNTS, encodeCounts(playCounts))
        addLearned(listOf(id))
        return firstTime
    }

    /** Record that the child viewed this word in flashcards. */
    fun noteStudied(id: String) {
        studyCounts = studyCounts + (id to (studyCounts[id] ?: 0) + 1)
        store.putString(K_STUDY_COUNTS, encodeCounts(studyCounts))
        addLearned(listOf(id))
    }

    /** Record the end of a game against Lettie. [learnedIds] are words the child played correctly. */
    fun recordGame(childWon: Boolean, chainLength: Int, learnedIds: List<String>) {
        gamesPlayed++
        store.putInt(K_GAMES, gamesPlayed)

        if (childWon) {
            gamesWon++
            store.putInt(K_WON, gamesWon)
        }
        if (chainLength > bestChain) {
            bestChain = chainLength
            store.putInt(K_BEST_CHAIN, bestChain)
        }
        updateStreak()
        addLearned(learnedIds)
    }

    fun recordQuiz(score: Int) {
        if (score > quizBest) {
            quizBest = score
            store.putInt(K_QUIZ_BEST, quizBest)
        }
        updateStreak()
    }

    fun reset() {
        gamesPlayed = 0; gamesWon = 0; bestChain = 0
        currentStreak = 0; bestStreak = 0; quizBest = 0
        wordsLearned = emptySet()
        playCounts = emptyMap()
        studyCounts = emptyMap()
        lastPlayedDay = -1L
        store.putInt(K_GAMES, 0)
        store.putInt(K_WON, 0)
        store.putInt(K_BEST_CHAIN, 0)
        store.putInt(K_STREAK, 0)
        store.putInt(K_BEST_STREAK, 0)
        store.putInt(K_QUIZ_BEST, 0)
        store.putString(K_LEARNED, "")
        store.putString(K_PLAY_COUNTS, "")
        store.putString(K_STUDY_COUNTS, "")
        store.putLong(K_LAST_DAY, -1L)
    }

    private fun updateStreak() {
        val today = currentEpochDay()
        currentStreak = when (lastPlayedDay) {
            today -> if (currentStreak == 0) 1 else currentStreak
            today - 1 -> currentStreak + 1
            else -> 1
        }
        lastPlayedDay = today
        store.putInt(K_STREAK, currentStreak)
        store.putLong(K_LAST_DAY, today)
        if (currentStreak > bestStreak) {
            bestStreak = currentStreak
            store.putInt(K_BEST_STREAK, bestStreak)
        }
    }

    private fun addLearned(ids: List<String>) {
        if (ids.isEmpty()) return
        val updated = wordsLearned + ids
        if (updated.size != wordsLearned.size) {
            wordsLearned = updated
            store.putString(K_LEARNED, updated.joinToString(SEP))
        }
    }

    private fun loadLearned(): Set<String> =
        store.getString(K_LEARNED)?.split(SEP)?.filter { it.isNotBlank() }?.toSet() ?: emptySet()

    private fun loadCounts(key: String): Map<String, Int> =
        store.getString(key)
            ?.split(SEP)
            ?.mapNotNull { entry ->
                val parts = entry.split(':')
                if (parts.size != 2) return@mapNotNull null
                val count = parts[1].toIntOrNull() ?: return@mapNotNull null
                if (parts[0].isBlank()) null else parts[0] to count
            }
            ?.toMap()
            ?: emptyMap()

    private fun encodeCounts(map: Map<String, Int>): String =
        map.entries.joinToString(SEP) { "${it.key}:${it.value}" }

    companion object {
        private const val SEP = ","
        private const val K_GAMES = "games_played"
        private const val K_WON = "games_won"
        private const val K_BEST_CHAIN = "best_chain"
        private const val K_STREAK = "current_streak"
        private const val K_BEST_STREAK = "best_streak"
        private const val K_QUIZ_BEST = "quiz_best"
        private const val K_LEARNED = "words_learned"
        private const val K_PLAY_COUNTS = "word_play_counts"
        private const val K_STUDY_COUNTS = "word_study_counts"
        private const val K_LAST_DAY = "last_played_day"
    }
}
