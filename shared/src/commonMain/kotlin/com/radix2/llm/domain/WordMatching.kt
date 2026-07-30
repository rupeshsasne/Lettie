package com.radix2.llm.domain

import kotlin.math.min

/**
 * Normalization + fuzzy matching tuned for young children's speech and ASR noise.
 * We match a spoken candidate against a *known* word (curated set), which is far more
 * reliable than open-domain recognition.
 */
object WordMatching {

    fun normalize(text: String): String =
        text.lowercase().trim().filter { it.isLetter() || it == ' ' }.replace(Regex("\\s+"), " ").trim()

    /** Compact form (letters only) for close comparisons. */
    private fun compact(text: String): String = text.lowercase().filter { it.isLetter() }

    /**
     * Does [spoken] plausibly refer to [word]? Checks exact name, aliases, and a
     * small edit-distance tolerance that scales with word length.
     */
    fun matches(spoken: String, word: Word): Boolean {
        val s = compact(spoken)
        if (s.isEmpty()) return false
        val targets = buildList {
            add(word.name)
            addAll(word.aliases)
        }.map { compact(it) }

        for (t in targets) {
            if (s == t) return true
            val tolerance = when {
                t.length <= 4 -> 1
                t.length <= 7 -> 2
                else -> 3
            }
            if (levenshtein(s, t) <= tolerance) return true
        }
        return false
    }

    fun levenshtein(a: String, b: String): Int {
        if (a == b) return 0
        if (a.isEmpty()) return b.length
        if (b.isEmpty()) return a.length
        var prev = IntArray(b.length + 1) { it }
        var curr = IntArray(b.length + 1)
        for (i in 1..a.length) {
            curr[0] = i
            for (j in 1..b.length) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                curr[j] = min(min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost)
            }
            val tmp = prev; prev = curr; curr = tmp
        }
        return prev[b.length]
    }
}
