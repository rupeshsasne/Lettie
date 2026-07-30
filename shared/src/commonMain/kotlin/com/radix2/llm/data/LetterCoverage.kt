package com.radix2.llm.data

import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Difficulty
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.Word

/**
 * QWERTY-style anti-jam coverage: frequent *ending* letters must have enough
 * *starting* words in the same contest round so chains rarely dead-end.
 */
object LetterCoverage {

    /** Top ending letters that must keep ≥ [minTopStarters] starters per round. */
    val round1TopEndings = listOf('E', 'A', 'N', 'Y', 'R', 'T')
    val round2TopEndings = listOf('A', 'E', 'N', 'D', 'R', 'Y')

    const val minTopStarters = 9
    const val minEasyExitStarters = 3

    fun wordsInRound(round: Round, all: List<Word> = WordData.all): List<Word> =
        all.filter { it.category in round.categories }

    fun starterCount(letter: Char, words: List<Word>): Int =
        words.count { it.firstLetter.equals(letter, ignoreCase = true) }

    fun endingCount(letter: Char, words: List<Word>): Int =
        words.count { it.lastLetter.equals(letter, ignoreCase = true) }

    fun topEndings(round: Round): List<Char> = when (round) {
        Round.ROUND_1 -> round1TopEndings
        Round.ROUND_2 -> round2TopEndings
    }

    /** Letters that appear as endings, sorted by frequency descending. */
    fun endingFrequency(words: List<Word>): List<Pair<Char, Int>> =
        words.groupingBy { it.lastLetter.uppercaseChar() }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .map { it.key to it.value }
}
