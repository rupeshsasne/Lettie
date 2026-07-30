package com.radix2.llm.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Favourite word ids, persisted via [KeyValueStore]. Exposes Compose snapshot state so
 * lists/detail screens recompose the moment a heart is toggled.
 */
class FavoritesStore(private val store: KeyValueStore) {

    var ids by mutableStateOf(load())
        private set

    fun isFavorite(id: String): Boolean = id in ids

    fun toggle(id: String) {
        ids = if (id in ids) ids - id else ids + id
        persist()
    }

    private fun load(): Set<String> =
        store.getString(KEY)?.split(SEP)?.filter { it.isNotBlank() }?.toSet() ?: emptySet()

    private fun persist() {
        store.putString(KEY, ids.joinToString(SEP))
    }

    companion object {
        private const val KEY = "favorite_ids"
        private const val SEP = ","
    }
}
