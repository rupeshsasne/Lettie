package com.radix2.llm.game

import com.radix2.llm.data.LetterCoverage
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.Word

/** A single multiple-choice quiz question with exactly one correct option. */
data class QuizQuestion(
    val prompt: String,
    val options: List<Word>,
    val answerId: String,
) {
    val answer: Word get() = options.first { it.id == answerId }
}

/**
 * Builds "Which animal starts with B?" / "Which fruit ends with E?" style questions.
 * Order is deterministic (no shuffle): walk a fixed curriculum favoring frequent ending letters.
 */
object QuizGenerator {

    fun generate(
        repo: WordRepository,
        count: Int = 8,
        categories: List<Category> = Category.entries.toList(),
        optionCount: Int = 4,
        startIndex: Int = 0,
    ): List<QuizQuestion> {
        val curriculum = buildCurriculum(repo, categories)
        if (curriculum.isEmpty()) return emptyList()

        val result = mutableListOf<QuizQuestion>()
        val seenPrompts = mutableSetOf<String>()
        var i = startIndex.mod(curriculum.size)
        var attempts = 0

        while (result.size < count && attempts < curriculum.size * 3) {
            attempts++
            val (answer, byStart) = curriculum[i]
            i = (i + 1) % curriculum.size
            val category = answer.category
            if (category !in categories) continue
            val pool = repo.forCategory(category)
            if (pool.size < optionCount) continue

            val clueLetter = if (byStart) answer.firstLetter else answer.lastLetter
            val distractors = pool
                .filter {
                    it.id != answer.id &&
                        (if (byStart) it.firstLetter != clueLetter else it.lastLetter != clueLetter)
                }
                .sortedBy { it.id }
            if (distractors.size < optionCount - 1) continue

            val prompt = if (byStart) {
                "Which ${category.singular} starts with '$clueLetter'?"
            } else {
                "Which ${category.singular} ends with '$clueLetter'?"
            }
            if (!seenPrompts.add(prompt)) continue

            val options = (distractors.take(optionCount - 1) + answer).sortedBy { it.id }
            result += QuizQuestion(prompt, options, answer.id)
        }
        return result
    }

    /**
     * Prefer practicing letters that often appear as endings (anti-jam drills),
     * Easy words first, stable id order within a letter.
     */
    private fun buildCurriculum(
        repo: WordRepository,
        categories: List<Category>,
    ): List<Pair<Word, Boolean>> {
        val rounds = Round.entries.filter { round ->
            round.categories.any { it in categories }
        }.ifEmpty { Round.entries }
        val endingPriority = rounds
            .flatMap { LetterCoverage.topEndings(it) }
            .distinct()

        val pool = repo.all
            .filter { it.category in categories }
            .sortedWith(
                compareBy<Word>(
                    { word ->
                        val idx = endingPriority.indexOf(word.firstLetter.uppercaseChar())
                        if (idx >= 0) idx else endingPriority.size
                    },
                    { it.difficulty.ordinal },
                    { it.category.ordinal },
                    { it.id },
                ),
            )

        val out = mutableListOf<Pair<Word, Boolean>>()
        for (word in pool) {
            out += word to true
            out += word to false
        }
        return out
    }
}
