package com.radix2.llm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private const val USER_AGENT =
    "LastLetterMaster/1.0 (https://github.com/rupeshsasne/Lettie; educational kids app)"

/**
 * Resolves a Wikipedia page thumbnail URL via the MediaWiki API.
 * Uses the exact URL Wikipedia returns (allowed thumbnail sizes only).
 */
actual suspend fun fetchWikipediaThumbnailUrl(title: String): String? = withContext(Dispatchers.IO) {
    runCatching {
        val encoded = URLEncoder.encode(title, Charsets.UTF_8.name())
        val endpoint = URL(
            "https://en.wikipedia.org/w/api.php" +
                "?action=query&format=json&formatversion=2" +
                "&prop=pageimages&piprop=thumbnail&pithumbsize=500" +
                "&redirects=1&titles=$encoded",
        )
        val conn = (endpoint.openConnection() as HttpURLConnection).apply {
            connectTimeout = 8_000
            readTimeout = 8_000
            requestMethod = "GET"
            setRequestProperty("User-Agent", USER_AGENT)
            setRequestProperty("Accept", "application/json")
        }
        try {
            if (conn.responseCode !in 200..299) return@runCatching null
            val body = conn.inputStream.bufferedReader().use { it.readText() }
            val pages = JSONObject(body)
                .optJSONObject("query")
                ?.optJSONArray("pages")
                ?: return@runCatching null
            if (pages.length() == 0) return@runCatching null
            val page = pages.getJSONObject(0)
            if (page.optBoolean("missing", false)) return@runCatching null
            page.optJSONObject("thumbnail")
                ?.optString("source")
                ?.takeIf { it.isNotBlank() }
        } finally {
            conn.disconnect()
        }
    }.getOrNull()
}
