package com.radix2.llm.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.ChainEntry
import com.radix2.llm.domain.Difficulty
import com.radix2.llm.domain.GameStatus
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.Speaker
import com.radix2.llm.domain.Word
import com.radix2.llm.data.ProgressStore
import com.radix2.llm.game.GameSession
import com.radix2.llm.game.SubmitResult
import com.radix2.llm.sound.SoundPlayer
import com.radix2.llm.voice.VoiceController
import kotlinx.coroutines.delay
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.ic_lightbulb
import lastlettermaster.shared.generated.resources.ic_mic
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    repo: WordRepository,
    voice: VoiceController,
    sound: SoundPlayer,
    progress: ProgressStore,
    round: Round,
    categories: List<Category>,
    difficulty: Difficulty,
    onOpenWord: (Word) -> Unit,
    onExit: () -> Unit,
) {
    val session = remember(round, categories, difficulty) {
        GameSession(repo, round, categories, difficulty)
    }

    var caption by remember { mutableStateOf("Get ready\u2026") }
    var childFeedback by remember { mutableStateOf<String?>(null) }
    var guesses by remember { mutableStateOf<List<Word>>(emptyList()) }
    var paused by remember { mutableStateOf(false) }
    var showTyping by remember { mutableStateOf(false) }
    var typed by remember { mutableStateOf("") }
    var confetti by remember { mutableStateOf(0) }

    val totalMs = difficulty.turnSeconds * 1000
    var timeLeftMs by remember { mutableStateOf(totalMs) }

    fun letterPhrase(c: Char) = "the letter $c"

    fun onChildWord(word: Word) {
        guesses = emptyList()
        childFeedback = null
        showTyping = false
        typed = ""
        sound.correct()
        confetti++
        voice.speak("Yes! ${word.name}!")
    }

    fun handleResult(result: SubmitResult) {
        when (result) {
            is SubmitResult.Accepted -> onChildWord(result.word)
            is SubmitResult.WrongLetter -> {
                sound.wrong()
                childFeedback = "${result.word.name} starts with ${result.word.firstLetter}. We need a word starting with ${result.required}."
                session.consumeRetry()
                voice.speak("${result.word.name} starts with ${result.word.firstLetter}. Try a word starting with ${letterPhrase(result.required)}.")
            }
            is SubmitResult.AlreadyUsed -> {
                sound.wrong()
                childFeedback = "We already used ${result.word.name}. Try another one."
                session.consumeRetry()
                voice.speak("We already used that one. Try another.")
            }
            is SubmitResult.NotRecognized -> {
                sound.wrong()
                guesses = result.guesses
                childFeedback = if (result.guesses.isEmpty()) {
                    "I didn't catch that. Tap and try again."
                } else {
                    "Did you mean one of these?"
                }
                session.consumeRetry()
            }
        }
    }

    fun handleCandidates(cands: List<String>) {
        for (c in cands) {
            val r = session.submitChild(c)
            if (r is SubmitResult.Accepted) { onChildWord(r.word); return }
            if (r is SubmitResult.WrongLetter || r is SubmitResult.AlreadyUsed) { handleResult(r); return }
        }
        handleResult(SubmitResult.NotRecognized(repo.guesses(cands.firstOrNull().orEmpty(), categories)))
    }

    LaunchedEffect(session) {
        val opener = session.start()
        timeLeftMs = totalMs
        if (opener != null) {
            caption = "Lettie played ${opener.name}."
            voice.speak("Let's play! My word is ${opener.name}. Your turn! Give me a word starting with ${letterPhrase(session.requiredLetter)}.")
        } else {
            caption = "You start!"
            voice.speak("You start! Give me a word starting with ${letterPhrase(session.requiredLetter)}.")
        }
    }

    LaunchedEffect(session.whoseTurn, session.status) {
        if (session.whoseTurn == Speaker.LETTIE && session.status == GameStatus.PLAYING) {
            caption = "Lettie is thinking\u2026"
            delay(1000)
            val played = session.lettieTurn()
            if (played != null) {
                caption = "Lettie played ${played.name}."
                voice.speak("My word is ${played.name}. Your turn! A word starting with ${letterPhrase(session.requiredLetter)}.")
            } else {
                caption = "Lettie is stuck. You win!"
                voice.speak("Oh no! I am stuck. You win!")
            }
        }
    }

    LaunchedEffect(session.whoseTurn, session.status, session.chain.size) {
        if (session.whoseTurn == Speaker.CHILD && session.status == GameStatus.PLAYING) {
            timeLeftMs = totalMs
            while (timeLeftMs > 0 &&
                session.whoseTurn == Speaker.CHILD &&
                session.status == GameStatus.PLAYING
            ) {
                delay(100)
                if (!paused && !voice.isListening && !voice.isSpeaking) {
                    timeLeftMs -= 100
                }
            }
            if (timeLeftMs <= 0 &&
                session.status == GameStatus.PLAYING &&
                session.whoseTurn == Speaker.CHILD
            ) {
                session.childStuck()
                caption = "Time's up! Good try."
                voice.speak("Time is up! Good try!")
            }
        }
    }

    LaunchedEffect(session.status) {
        when (session.status) {
            GameStatus.CHILD_WON -> {
                sound.win()
                confetti++
                progress.recordGame(
                    childWon = true,
                    chainLength = session.chain.size,
                    learnedIds = session.chain.filter { it.speaker == Speaker.CHILD }.map { it.word.id },
                )
            }
            GameStatus.LETTIE_WON -> {
                progress.recordGame(
                    childWon = false,
                    chainLength = session.chain.size,
                    learnedIds = session.chain.filter { it.speaker == Speaker.CHILD }.map { it.word.id },
                )
            }
            else -> Unit
        }
    }

    val childTurn = session.whoseTurn == Speaker.CHILD && session.status == GameStatus.PLAYING

    AppScaffold(title = round.displayName, onBack = onExit) { innerPadding ->
      Box(Modifier.fillMaxSize().padding(innerPadding)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = caption,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(16.dp))
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Your word starts with",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = if (session.requiredLetter == ' ') "?" else session.requiredLetter.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    if (childTurn) {
                        Spacer(Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { (timeLeftMs.toFloat() / totalMs).coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "${(timeLeftMs + 999) / 1000} seconds left",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            if (session.chain.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                WordChain(session.chain, onOpenWord = onOpenWord)
            }

            childFeedback?.let {
                Spacer(Modifier.height(16.dp))
                Text(
                    it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            }

            if (guesses.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(guesses) { word ->
                        AssistChip(
                            onClick = { handleResult(session.submitConfirmed(word)) },
                            label = { Text(word.name) },
                        )
                    }
                }
            }

            if (showTyping) {
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = typed,
                        onValueChange = { typed = it },
                        label = { Text("Type a word") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    Button(onClick = { if (typed.isNotBlank()) handleCandidates(listOf(typed)) }) {
                        Text("Go")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            ExtendedFloatingActionButton(
                text = { Text(if (voice.isListening) "Listening\u2026" else "Tap & Speak") },
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_mic),
                        contentDescription = null,
                    )
                },
                onClick = {
                    if (childTurn) {
                        sound.tap()
                        guesses = emptyList()
                        childFeedback = null
                        voice.startListening(
                            onResult = { handleCandidates(it) },
                            onError = { msg ->
                                childFeedback = msg
                                if (!voice.recognitionAvailable) showTyping = true
                            },
                        )
                    }
                },
                containerColor = if (voice.isListening) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                },
            )

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(
                    onClick = {
                        val hint = session.hintWord()
                        childFeedback = if (hint != null) {
                            voice.speak("Here's a hint. Try a word starting with ${letterPhrase(session.requiredLetter)}, like ${hint.name}.")
                            "Try a word starting with ${session.requiredLetter}, like ${hint.name}."
                        } else {
                            "That's a tricky letter!"
                        }
                    },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_lightbulb),
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Hint")
                }
                TextButton(onClick = { showTyping = !showTyping }) {
                    Text("Type instead")
                }
            }
        }
        ConfettiOverlay(trigger = confetti, modifier = Modifier.fillMaxSize())
      }
    }

    if (session.status == GameStatus.CHILD_WON || session.status == GameStatus.LETTIE_WON) {
        val childWon = session.status == GameStatus.CHILD_WON
        AlertDialog(
            onDismissRequest = { },
            title = { Text(if (childWon) "You won!" else "Good try!") },
            text = {
                Text("You played ${session.childWordCount} word${if (session.childWordCount == 1) "" else "s"}.")
            },
            confirmButton = {
                TextButton(onClick = {
                    guesses = emptyList()
                    childFeedback = null
                    showTyping = false
                    typed = ""
                    val opener = session.start()
                    timeLeftMs = totalMs
                    if (opener != null) {
                        caption = "Lettie played ${opener.name}."
                        voice.speak("Let's play again! My word is ${opener.name}. Your turn!")
                    } else {
                        caption = "You start!"
                        voice.speak("Let's play again! You start!")
                    }
                }) {
                    Text("Play again")
                }
            },
            dismissButton = {
                TextButton(onClick = onExit) { Text("Home") }
            },
        )
    }
}

@Composable
private fun WordChain(chain: List<ChainEntry>, onOpenWord: (Word) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(chain) { entry ->
            AssistChip(
                onClick = { onOpenWord(entry.word) },
                label = {
                    Text("${if (entry.speaker == Speaker.LETTIE) "Lettie" else "You"}: ${entry.word.name}")
                },
            )
        }
    }
}
