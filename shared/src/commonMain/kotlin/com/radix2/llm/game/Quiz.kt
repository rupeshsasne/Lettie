package com.radix2.llm.game

import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Word
import kotlin.random.Random

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
 * Distractors are drawn from the same category but are guaranteed NOT to satisfy the
 * clue, so there is always exactly one correct answer.
 */
object QuizGenerator {

    fun generate(
        repo: WordRepository,
        count: Int = 8,
        categories: List<Category> = Category.entries.toList(),
        optionCount: Int = 4,
        random: Random = Random.Default,
    ): List<QuizQuestion> {
        val result = mutableListOf<QuizQuestion>()
        var attempts = 0
        val seenPrompts = mutableSetOf<String>()

        while (result.size < count && attempts < count * 40) {
            attempts++
            val category = categories.random(random)
            val pool = repo.forCategory(category)
            if (pool.size < optionCount) continue

            val answer = pool.random(random)
            val byStart = random.nextBoolean()
            val clueLetter = if (byStart) answer.firstLetter else answer.lastLetter

            val distractors = pool.filter {
                it.id != answer.id &&
                    (if (byStart) it.firstLetter != clueLetter else it.lastLetter != clueLetter)
            }
            if (distractors.size < optionCount - 1) continue

            val prompt = if (byStart) {
                "Which ${category.singular} starts with '$clueLetter'?"
            } else {
                "Which ${category.singular} ends with '$clueLetter'?"
            }
            if (!seenPrompts.add(prompt)) continue

            val options = (distractors.shuffled(random).take(optionCount - 1) + answer)
                .shuffled(random)
            result += QuizQuestion(prompt, options, answer.id)
        }
        return result
    }
}
