package com.radix2.llm.navigation

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.radix2.llm.domain.Category

/** App navigation destinations — encode/decode for configuration-change survival. */
sealed interface Screen {
    data object Home : Screen
    data class Library(val category: Category? = null) : Screen
    data class Detail(val wordId: String) : Screen
    data object Setup : Screen
    data object Game : Screen
    data object Quiz : Screen
    /**
     * @param focusLetter if set, flashcards open as a practice drill for that starter letter
     * @param categoryNames optional category filter (enum names); empty = all / round chips
     */
    data class Flashcards(
        val focusLetter: Char? = null,
        val categoryNames: List<String> = emptyList(),
    ) : Screen
    data object Progress : Screen
    data object Voice : Screen

    fun encode(): String = when (this) {
        Home -> "home"
        is Library -> "library:${category?.name.orEmpty()}"
        is Detail -> "detail:$wordId"
        Setup -> "setup"
        Game -> "game"
        Quiz -> "quiz"
        is Flashcards -> buildString {
            append("flashcards")
            if (focusLetter != null || categoryNames.isNotEmpty()) {
                append(':')
                append(focusLetter?.uppercaseChar()?.toString().orEmpty())
                if (categoryNames.isNotEmpty()) {
                    append(':')
                    append(categoryNames.joinToString("|"))
                }
            }
        }
        Progress -> "progress"
        Voice -> "voice"
    }

    companion object {
        fun decode(raw: String): Screen {
            val parts = raw.split(':', limit = 3)
            return when (parts[0]) {
                "home" -> Home
                "library" -> Library(
                    parts.getOrNull(1)
                        ?.takeIf { it.isNotBlank() }
                        ?.let { name -> Category.entries.find { it.name == name } },
                )
                "detail" -> Detail(parts.getOrElse(1) { "" })
                "setup" -> Setup
                "game" -> Game
                "quiz" -> Quiz
                "flashcards" -> Flashcards(
                    focusLetter = parts.getOrNull(1)
                        ?.firstOrNull()
                        ?.takeIf { it.isLetter() }
                        ?.uppercaseChar(),
                    categoryNames = parts.getOrNull(2)
                        ?.split('|')
                        ?.filter { it.isNotBlank() }
                        .orEmpty(),
                )
                "progress" -> Progress
                "voice" -> Voice
                else -> Home
            }
        }

        /** Plain [List] saver — avoids SnapshotStateList (Android Parcelable) in commonMain. */
        val BackStackSaver: Saver<List<Screen>, Any> = listSaver(
            save = { stack -> stack.map { it.encode() } },
            restore = { encoded ->
                encoded.map { decode(it.toString()) }.ifEmpty { listOf(Home) }
            },
        )
    }
}

val Screen.saveKey: String
    get() = when (this) {
        is Screen.Home -> "home"
        is Screen.Library -> "library"
        is Screen.Detail -> "detail:${wordId}"
        is Screen.Setup -> "setup"
        is Screen.Game -> "game"
        is Screen.Quiz -> "quiz"
        is Screen.Flashcards -> encode()
        is Screen.Progress -> "progress"
        is Screen.Voice -> "voice"
    }
