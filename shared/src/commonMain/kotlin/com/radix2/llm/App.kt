package com.radix2.llm

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.radix2.llm.data.FavoritesStore
import com.radix2.llm.data.ProgressStore
import com.radix2.llm.data.WordRepository
import com.radix2.llm.data.rememberKeyValueStore
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Difficulty
import com.radix2.llm.domain.Round
import com.radix2.llm.game.GameSession
import com.radix2.llm.navigation.Screen
import com.radix2.llm.navigation.saveKey
import com.radix2.llm.sound.rememberSoundPlayer
import com.radix2.llm.ui.FlashcardsScreen
import com.radix2.llm.ui.GameScreen
import com.radix2.llm.ui.HomeScreen
import com.radix2.llm.ui.LibraryScreen
import com.radix2.llm.ui.MainScaffold
import com.radix2.llm.ui.ProgressScreen
import com.radix2.llm.ui.QuizScreen
import com.radix2.llm.ui.SetupImageLoader
import com.radix2.llm.ui.SetupScreen
import com.radix2.llm.ui.SystemBackHandler
import com.radix2.llm.ui.VoiceSettingsScreen
import com.radix2.llm.ui.WordDetailScreen
import com.radix2.llm.ui.adaptive.LettieDestination
import com.radix2.llm.ui.adaptive.ListDetailLayout
import com.radix2.llm.ui.adaptive.LocalWindowSize
import com.radix2.llm.ui.adaptive.ProvideWindowSize
import com.radix2.llm.ui.theme.LastLetterTheme
import com.radix2.llm.ui.theme.LettieDimens
import com.radix2.llm.voice.rememberVoiceController

private const val PrefTtsVoiceId = "tts_voice_id"

@Composable
fun App() {
    SetupImageLoader()
    LastLetterTheme {
        ProvideWindowSize {
            val repo = remember { WordRepository() }
            val store = rememberKeyValueStore()
            val voice = rememberVoiceController(
                preferredVoiceId = store.getString(PrefTtsVoiceId),
                onVoiceSelected = { store.putString(PrefTtsVoiceId, it) },
            )
            val sound = rememberSoundPlayer()
            val favorites = remember { FavoritesStore(store) }
            val progress = remember { ProgressStore(store) }

            // Survives rotation via Activity configChanges; also kept across light recreations.
            var gameSession by remember { mutableStateOf<GameSession?>(null) }

            // Critical: back stack must be saveable — otherwise rotate = brand-new app.
            val backStack = rememberSaveable(saver = Screen.BackStackSaver) {
                androidx.compose.runtime.mutableStateListOf<Screen>(Screen.Home)
            }
            val current = backStack.last()
            val saveableStateHolder = rememberSaveableStateHolder()
            val window = LocalWindowSize.current

            fun navigate(screen: Screen) { backStack.add(screen) }
            fun back() { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }
            fun home() {
                gameSession = null
                backStack.clear()
                backStack.add(Screen.Home)
            }
            fun goPrimary(screen: Screen) {
                gameSession = null
                backStack.clear()
                backStack.add(screen)
            }

            fun onDestinationSelected(dest: LettieDestination) {
                when (dest) {
                    LettieDestination.Play -> goPrimary(Screen.Home)
                    LettieDestination.Words -> goPrimary(Screen.Library())
                    LettieDestination.Learn -> goPrimary(Screen.Flashcards())
                    LettieDestination.Me -> goPrimary(Screen.Progress)
                }
            }

            val selectedDest: LettieDestination? = when (current) {
                is Screen.Home, is Screen.Setup, is Screen.Game -> LettieDestination.Play
                is Screen.Library, is Screen.Detail -> LettieDestination.Words
                is Screen.Flashcards, is Screen.Quiz -> LettieDestination.Learn
                is Screen.Progress, is Screen.Voice -> LettieDestination.Me
            }

            SystemBackHandler(enabled = backStack.size > 1) { back() }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                saveableStateHolder.SaveableStateProvider(current.saveKey) {
                    val dualLibrary = window.useDualPane &&
                        (current is Screen.Library || current is Screen.Detail)

                    when {
                        dualLibrary -> {
                            val detailId = (current as? Screen.Detail)?.wordId
                            MainScaffold(
                                selected = LettieDestination.Words,
                                onDestinationSelected = ::onDestinationSelected,
                                title = "Words",
                            ) { padding ->
                                Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                                    ListDetailLayout(
                                        showDetail = detailId != null,
                                        list = {
                                            LibraryScreen(
                                                repo = repo,
                                                voice = voice,
                                                favorites = favorites,
                                                initialCategory = null,
                                                onOpenWord = { navigate(Screen.Detail(it.id)) },
                                                showScaffold = false,
                                            )
                                        },
                                        detail = {
                                            val word = detailId?.let { repo.byId(it) }
                                            if (word == null) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(LettieDimens.screenPadding),
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    Text(
                                                        "Pick a word to learn more",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    )
                                                }
                                            } else {
                                                WordDetailScreen(
                                                    word = word,
                                                    repo = repo,
                                                    favorites = favorites,
                                                    voice = voice,
                                                    onOpenWord = { navigate(Screen.Detail(it.id)) },
                                                    showScaffold = false,
                                                )
                                            }
                                        },
                                    )
                                }
                            }
                        }

                        else -> when (val screen = current) {
                            is Screen.Home -> HomeScreen(
                                onPlay = { navigate(Screen.Setup) },
                                onExplore = { goPrimary(Screen.Library()) },
                                onQuiz = { navigate(Screen.Quiz) },
                                onFlashcards = { goPrimary(Screen.Flashcards()) },
                                onProgress = { goPrimary(Screen.Progress) },
                                onVoice = { navigate(Screen.Voice) },
                                selectedDestination = LettieDestination.Play,
                                onDestinationSelected = ::onDestinationSelected,
                            )

                            is Screen.Library -> LibraryScreen(
                                repo = repo,
                                voice = voice,
                                favorites = favorites,
                                initialCategory = screen.category,
                                onOpenWord = { navigate(Screen.Detail(it.id)) },
                                onBack = { back() },
                                selectedDestination = LettieDestination.Words,
                                onDestinationSelected = ::onDestinationSelected,
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
                                    gameSession = GameSession(
                                        repo = repo,
                                        round = round,
                                        activeCategories = categories,
                                        difficulty = difficulty,
                                        childPlayCount = { progress.playCount(it) },
                                        childLetterStrength = { progress.letterStrength(it) },
                                    )
                                    navigate(Screen.Game)
                                },
                                onBack = { back() },
                            )

                            is Screen.Game -> {
                                val session = gameSession
                                if (session == null) {
                                    // Process death mid-game — return safely rather than crash.
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
                                        onPracticeLetter = { letter, categories ->
                                            goPrimary(
                                                Screen.Flashcards(
                                                    focusLetter = letter,
                                                    categoryNames = categories.map { it.name },
                                                ),
                                            )
                                        },
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
                                progressStore = progress,
                                focusLetter = screen.focusLetter,
                                focusCategoryNames = screen.categoryNames,
                                onExit = { goPrimary(Screen.Home) },
                                selectedDestination = selectedDest,
                                onDestinationSelected = ::onDestinationSelected,
                            )

                            is Screen.Progress -> ProgressScreen(
                                progress = progress,
                                onExit = { goPrimary(Screen.Home) },
                                selectedDestination = selectedDest,
                                onDestinationSelected = ::onDestinationSelected,
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
}
