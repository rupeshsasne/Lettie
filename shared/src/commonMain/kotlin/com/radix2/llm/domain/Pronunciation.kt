package com.radix2.llm.domain

/**
 * Kid-friendly "say it like" hints. Uses an explicit [Word.syllables] when present;
 * otherwise builds a simple hyphenated reading from the name.
 */
object Pronunciation {
    fun sayItLike(word: Word): String =
        word.syllables?.takeIf { it.isNotBlank() } ?: autoSyllabify(word.name)

    fun autoSyllabify(name: String): String {
        val parts = name.split(Regex("[\\s-]+")).filter { it.isNotBlank() }
        if (parts.isEmpty()) return name.lowercase()
        return parts.joinToString(" · ") { syllabifyToken(it.filter { ch -> ch.isLetter() }) }
            .ifBlank { name.lowercase() }
    }

    private fun syllabifyToken(raw: String): String {
        val w = raw.lowercase()
        if (w.length <= 3) return w
        val vowels = setOf('a', 'e', 'i', 'o', 'u', 'y')
        val cuts = mutableListOf<Int>()
        var i = 1
        while (i < w.length) {
            val prevVowel = w[i - 1] in vowels
            val curVowel = w[i] in vowels
            // New syllable when a vowel follows a consonant (skip leading vowel cluster).
            if (!prevVowel && curVowel && i > 0) {
                cuts.add(i)
            }
            i++
        }
        if (cuts.isEmpty()) return w
        val out = StringBuilder()
        var start = 0
        for (cut in cuts) {
            if (cut - start < 1) continue
            if (out.isNotEmpty()) out.append('-')
            out.append(w, start, cut)
            start = cut
        }
        if (start < w.length) {
            if (out.isNotEmpty()) out.append('-')
            out.append(w, start, w.length)
        }
        return out.toString().trim('-').ifBlank { w }
    }
}

/** Always exactly two kid-friendly fun facts for the detail “About” card. */
object KidFacts {
    fun forWord(word: Word): List<String> {
        FunFacts.forId(word.id)?.takeIf { it.size >= 2 }?.let { return it.take(2) }
        val curated = word.facts.filter { it.isNotBlank() }
        if (curated.size >= 2) return curated.take(2)
        // Should not happen once FunFacts covers the bank; keep two non-empty lines.
        val fallback = curated + listOf(
            "${word.name} has a cool story waiting to be discovered!",
            "Ask a grown-up to help you find another fun fact about ${word.name}.",
        )
        return fallback.take(2)
    }
}
