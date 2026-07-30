package com.radix2.llm.data

import androidx.compose.runtime.Composable

/**
 * Tiny multiplatform key-value store for lightweight persistence (favourites, progress).
 * Backed by SharedPreferences on Android. Not meant for large data — for that we'd move
 * to a real database (see PRD data-architecture note).
 */
interface KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun getInt(key: String, default: Int): Int
    fun putInt(key: String, value: Int)
    fun getLong(key: String, default: Long): Long
    fun putLong(key: String, value: Long)
}

/** Days since the Unix epoch in the device's local time — used for daily-streak math. */
expect fun currentEpochDay(): Long

@Composable
expect fun rememberKeyValueStore(): KeyValueStore

/** In-memory store for previews/tests. */
class InMemoryKeyValueStore : KeyValueStore {
    private val map = mutableMapOf<String, String>()
    override fun getString(key: String): String? = map[key]
    override fun putString(key: String, value: String) { map[key] = value }
    override fun getInt(key: String, default: Int): Int = map[key]?.toIntOrNull() ?: default
    override fun putInt(key: String, value: Int) { map[key] = value.toString() }
    override fun getLong(key: String, default: Long): Long = map[key]?.toLongOrNull() ?: default
    override fun putLong(key: String, value: Long) { map[key] = value.toString() }
}
