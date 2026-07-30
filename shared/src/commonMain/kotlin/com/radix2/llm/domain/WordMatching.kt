package com.radix2.llm.domain

import kotlin.math.min

/**
 * Normalization + fuzzy matching tuned for young children's speech and ASR noise.
 * Exact name/alias matches always win over fuzzy — otherwise near-neighbours like
 * "eggplant" / "elephant" steal each other's turns.
 */
object WordMatching {

    fun normalize(text: String): String =
        text.lowercase().trim().filter { it.isLetter() || it == ' ' }.replace(Regex("\\s+"), " ").trim()

    /** Compact form (letters only) for close comparisons. */
    fun compact(text: String): String = text.lowercase().filter { it.isLetter() }

    /** Exact match against the word's name or any alias (ignoring spaces/case). */
    fun exactMatch(spoken: String, word: Word): Boolean {
        val s = compact(spoken)
        if (s.isEmpty()) return false
        if (s == compact(word.name)) return true
        return word.aliases.any { compact(it) == s }
    }

    /**
     * Does [spoken] plausibly refer to [word]? Exact first, then a tight edit-distance
     * tolerance that scales with word length (kept strict so similar words don't collide).
     */
    fun matches(spoken: String, word: Word): Boolean {
        if (exactMatch(spoken, word)) return true
        val s = compact(spoken)
        if (s.isEmpty()) return false
        val targets = buildList {
            add(word.name)
            addAll(word.aliases)
        }.map { compact(it) }

        for (t in targets) {
            val tolerance = fuzzyTolerance(t.length)
            if (levenshtein(s, t) <= tolerance) return true
        }
        return false
    }

    /** Best matching word for [spoken] among [candidates], preferring exact then closest fuzzy. */
    fun bestMatch(spoken: String, candidates: Collection<Word>): Word? {
        if (candidates.isEmpty()) return null
        val exact = candidates.firstOrNull { exactMatch(spoken, it) }
        if (exact != null) return exact

        val s = compact(spoken)
        if (s.isEmpty()) return null

        var best: Word? = null
        var bestDist = Int.MAX_VALUE
        for (word in candidates) {
            val targets = buildList {
                add(word.name)
                addAll(word.aliases)
            }.map { compact(it) }
            for (t in targets) {
                val d = levenshtein(s, t)
                if (d <= fuzzyTolerance(t.length) && d < bestDist) {
                    bestDist = d
                    best = word
                }
            }
        }
        return best
    }

    /** Tight tolerances: length-8 neighbours like eggplant/elephant (dist 3) must NOT match. */
    private fun fuzzyTolerance(length: Int): Int = when {
        length <= 4 -> 1
        length <= 7 -> 1
        else -> 2
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
