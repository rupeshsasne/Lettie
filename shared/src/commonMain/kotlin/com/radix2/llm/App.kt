package com.radix2.llm

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.radix2.llm.data.FavoritesStore
import com.radix2.llm.data.ProgressStore
import com.radix2.llm.data.WordRepository
import com.radix2.llm.data.rememberKeyValueStore
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Difficulty
import com.radix2.llm.domain.Round
import com.radix2.llm.game.GameSession
import com.radix2.llm.sound.rememberSoundPlayer
import com.radix2.llm.ui.FlashcardsScreen
import com.radix2.llm.ui.GameScreen
import com.radix2.llm.ui.HomeScreen
import com.radix2.llm.ui.LibraryScreen
import com.radix2.llm.ui.ProgressScreen
import com.radix2.llm.ui.QuizScreen
import com.radix2.llm.ui.SetupImageLoader
import com.radix2.llm.ui.SetupScreen
import com.radix2.llm.ui.SystemBackHandler
import com.radix2.llm.ui.VoiceSettingsScreen
import com.radix2.llm.ui.WordDetailScreen
import com.radix2.llm.ui.theme.LastLetterTheme
import com.radix2.llm.voice.rememberVoiceController

private const val PrefTtsVoiceId = "tts_voice_id"

/** App navigation destinations. */
sealed interface Screen {
    data object Home : Screen
    data class Library(val category: Category? = null) : Screen
    data class Detail(val wordId: String) : Screen
    data object Setup : Screen
    data object Game : Screen
    data object Quiz : Screen
    data object Flashcards : Screen
    data object Progress : Screen
    data object Voice : Screen
}

@Composable
fun App() {
    SetupImageLoader()
    LastLetterTheme {
        val repo = remember { WordRepository() }
        val store = rememberKeyValueStore()
        val voice = rememberVoiceController(
            preferredVoiceId = store.getString(PrefTtsVoiceId),
            onVoiceSelected = { store.putString(PrefTtsVoiceId, it) },
        )
        val sound = rememberSoundPlayer()
        val favorites = remember { FavoritesStore(store) }
        val progress = remember { ProgressStore(store) }

        // Survives leaving Game → Detail → back so the match can resume.
        var gameSession by remember { mutableStateOf<GameSession?>(null) }

        val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }
        val current = backStack.last()
        val saveableStateHolder = rememberSaveableStateHolder()

        fun navigate(screen: Screen) { backStack.add(screen) }
        fun back() { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }
        fun home() {
            gameSession = null
            backStack.clear()
            backStack.add(Screen.Home)
        }

        SystemBackHandler(enabled = backStack.size > 1) { back() }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Box(Modifier.fillMaxSize()) {
                // Keyed saveable buckets keep Library scroll/filters across Detail.
                saveableStateHolder.SaveableStateProvider(current.saveKey) {
                    when (val screen = current) {
                        is Screen.Home -> HomeScreen(
                            onPlay = { navigate(Screen.Setup) },
                            onExplore = { navigate(Screen.Library()) },
                            onQuiz = { navigate(Screen.Quiz) },
                            onFlashcards = { navigate(Screen.Flashcards) },
                            onProgress = { navigate(Screen.Progress) },
                            onVoice = { navigate(Screen.Voice) },
                        )

                        is Screen.Library -> LibraryScreen(
                            repo = repo,
                            voice = voice,
                            favorites = favorites,
                            initialCategory = screen.category,
                            onOpenWord = { navigate(Screen.Detail(it.id)) },
                            onBack = { back() },
                        )

                        is Screen.Detail -> {
                            val word = repo.byId(screen.wordId)
                            if (word == null) {
                                back()
                            } else {
                                WordDetailScreen(
                                    word = word,
                                    repo = repo,
                                    favorites = favorites,
                                    voice = voice,
                                    onOpenWord = { navigate(Screen.Detail(it.id)) },
                                    onBack = { back() },
                                )
                            }
                        }

                        is Screen.Setup -> SetupScreen(
                            repo = repo,
                            onStart = { round, categories, difficulty ->
                                gameSession = GameSession(repo, round, categories, difficulty)
                                navigate(Screen.Game)
                            },
                            onBack = { back() },
                        )

                        is Screen.Game -> {
                            val session = gameSession
                            if (session == null) {
                                home()
                            } else {
                                GameScreen(
                                    session = session,
                                    repo = repo,
                                    voice = voice,
                                    sound = sound,
                                    progress = progress,
                                    onOpenWord = { navigate(Screen.Detail(it.id)) },
                                    onExit = { home() },
                                )
                            }
                        }

                        is Screen.Quiz -> QuizScreen(
                            repo = repo,
                            sound = sound,
                            progress = progress,
                            onExit = { back() },
                        )

                        is Screen.Flashcards -> FlashcardsScreen(
                            repo = repo,
                            voice = voice,
                            onExit = { back() },
                        )

                        is Screen.Progress -> ProgressScreen(
                            progress = progress,
                            onExit = { back() },
                        )

                        is Screen.Voice -> VoiceSettingsScreen(
                            voice = voice,
                            onBack = { back() },
                        )
                    }
                }
            }
        }
    }
}

/** Stable keys so [rememberSaveableStateHolder] can restore Library (and friends). */
private val Screen.saveKey: String
    get() = when (this) {
        is Screen.Home -> "home"
        is Screen.Library -> "library"
        is Screen.Detail -> "detail" // shared bucket so back restores prior screen, not each word
        is Screen.Setup -> "setup"
        is Screen.Game -> "game"
        is Screen.Quiz -> "quiz"
        is Screen.Flashcards -> "flashcards"
        is Screen.Progress -> "progress"
        is Screen.Voice -> "voice"
    }
