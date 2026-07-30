package com.radix2.llm.domain

/** The seven contest categories. */
enum class Category(
    val displayName: String,
    val singular: String,
    val emoji: String,
) {
    ANIMAL("Animals", "animal", "\uD83E\uDD81"),
    BIRD("Birds", "bird", "\uD83D\uDC26"),
    FRUIT("Fruits", "fruit", "\uD83C\uDF4E"),
    VEGETABLE("Vegetables", "vegetable", "\uD83E\uDD55"),
    FLOWER("Flowers", "flower", "\uD83C\uDF3B"),
    CITY("Cities", "city", "\uD83C\uDFD9\uFE0F"),
    COUNTRY("Countries", "country", "\uD83C\uDF0D"),
}

/** Contest rounds map to a set of allowed categories. */
enum class Round(
    val displayName: String,
    val categories: List<Category>,
) {
    ROUND_1(
        "Round 1",
        listOf(Category.ANIMAL, Category.BIRD, Category.FRUIT, Category.VEGETABLE, Category.FLOWER),
    ),
    ROUND_2(
        "Round 2",
        listOf(Category.CITY, Category.COUNTRY),
    ),
}

/**
 * Difficulty controls Lettie's word choices and the turn timer.
 * Timer is ON by design (adds excitement); can be disabled in settings.
 */
enum class Difficulty(
    val label: String,
    val turnSeconds: Int,
    val retries: Int,
) {
    EASY("Easy", 30, 3),
    MEDIUM("Medium", 20, 2),
    HARD("Hard", 12, 1),
}

enum class Speaker { CHILD, LETTIE }

enum class GameStatus { PLAYING, CHILD_WON, LETTIE_WON, PAUSED }

/** Optional geography details for cities/countries. */
data class Geo(
    val capital: String? = null,
    val countryOf: String? = null,
    val funFact: String? = null,
)

/**
 * A single word entry.
 * [imageUrl] is an optional direct photograph URL (loaded at runtime).
 * When null, [WordImages] resolves a Wikipedia thumbnail URL for [wikiTitle] / [name].
 */
data class Word(
    val id: String,
    val name: String,
    val category: Category,
    val emoji: String,
    val difficulty: Difficulty = Difficulty.EASY,
    val syllables: String? = null,
    val facts: List<String> = emptyList(),
    val aliases: List<String> = emptyList(),
    val geo: Geo? = null,
    val imageUrl: String? = null,
    val wikiTitle: String? = null,
) {
    /** Lowercase letters only, used for matching. */
    val normalized: String get() = name.lowercase().filter { it.isLetter() }

    /** First playable letter of the word (uppercase). */
    val firstLetter: Char get() = normalized.firstOrNull()?.uppercaseChar() ?: '?'

    /** Last playable letter of the word (uppercase) — the letter the next word must start with. */
    val lastLetter: Char get() = normalized.lastOrNull()?.uppercaseChar() ?: '?'
}

data class ChainEntry(
    val word: Word,
    val speaker: Speaker,
)
