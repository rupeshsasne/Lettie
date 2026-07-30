package com.radix2.llm.data

import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Word
import com.radix2.llm.domain.WordMatching

/**
 * In-memory, offline word repository backed by the curated dataset in [WordData].
 * Provides lookups used by both the library browser and the game engine.
 */
class WordRepository(
    private val words: List<Word> = WordData.all,
) {
    private val byCategory: Map<Category, List<Word>> =
        words.groupBy { it.category }.mapValues { (_, list) -> list.sortedBy { it.name.lowercase() } }

    val all: List<Word> get() = words

    fun forCategory(category: Category): List<Word> = byCategory[category].orEmpty()

    fun count(category: Category): Int = byCategory[category]?.size ?: 0

    /** Words in [categories], grouped A-Z by first letter, each group alphabetized. */
    fun alphabetical(categories: Collection<Category>): Map<Char, List<Word>> =
        words.asSequence()
            .filter { it.category in categories }
            .sortedBy { it.name.lowercase() }
            .groupBy { it.firstLetter }
            .toSortedMap()

    fun search(query: String, categories: Collection<Category> = Category.entries): List<Word> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        return words.filter { it.category in categories && it.name.lowercase().contains(q) }
            .sortedBy { it.name.lowercase() }
    }

    fun byId(id: String): Word? = words.firstOrNull { it.id == id }

    /** All unused words in [categories] that start with [letter]. */
    fun startingWith(
        letter: Char,
        categories: Collection<Category>,
        excludingIds: Set<String>,
    ): List<Word> = words.filter {
        it.category in categories &&
            it.firstLetter.equals(letter, ignoreCase = true) &&
            it.id !in excludingIds
    }

    /** Find a curated word matching spoken text within [categories]. Exact match wins over fuzzy. */
    fun findSpoken(
        spoken: String,
        categories: Collection<Category>,
    ): Word? = WordMatching.bestMatch(spoken, words.filter { it.category in categories })

    /** Best-guess candidates for a low-confidence spoken input (for confirm chips). */
    fun guesses(
        spoken: String,
        categories: Collection<Category>,
        limit: Int = 3,
    ): List<Word> {
        val s = WordMatching.compact(spoken)
        if (s.isEmpty()) return emptyList()
        return words.asSequence()
            .filter { it.category in categories }
            .map { word ->
                val targets = buildList {
                    add(word.name)
                    addAll(word.aliases)
                }.map { WordMatching.compact(it) }
                val dist = targets.minOf { WordMatching.levenshtein(s, it) }
                word to dist
            }
            .sortedBy { it.second }
            .take(limit)
            .map { it.first }
            .toList()
    }
}
