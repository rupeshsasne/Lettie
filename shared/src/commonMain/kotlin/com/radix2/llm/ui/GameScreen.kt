package com.radix2.llm.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radix2.llm.data.ProgressStore
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.ChainEntry
import com.radix2.llm.domain.GameStatus
import com.radix2.llm.domain.Speaker
import com.radix2.llm.domain.Word
import com.radix2.llm.game.GameSession
import com.radix2.llm.game.SubmitResult
import com.radix2.llm.sound.SoundPlayer
import com.radix2.llm.voice.VoiceController
import com.radix2.llm.voice.awaitSilent
import com.radix2.llm.voice.speakAwait
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.ic_lightbulb
import lastlettermaster.shared.generated.resources.ic_mic
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    session: GameSession,
    repo: WordRepository,
    voice: VoiceController,
    sound: SoundPlayer,
    progress: ProgressStore,
    onOpenWord: (Word) -> Unit,
    onExit: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    var caption by remember { mutableStateOf("Get ready\u2026") }
    var childFeedback by remember { mutableStateOf<String?>(null) }
    var guesses by remember { mutableStateOf<List<Word>>(emptyList()) }
    var paused by remember { mutableStateOf(false) }
    var showTyping by remember { mutableStateOf(false) }
    var typed by remember { mutableStateOf("") }
    var confetti by remember { mutableStateOf(0) }
    var celebrateWord by remember { mutableStateOf<Word?>(session.lastWord) }
    var ready by remember { mutableStateOf(session.hasBegun) }
    /** Chain size we already auto-listened for — one auto-start per child turn. */
    var autoListenAtChainSize by remember { mutableStateOf(-1) }
    var lettieJob by remember { mutableStateOf<Job?>(null) }

    val totalMs = session.difficulty.turnSeconds * 1000

    fun letterPhrase(c: Char) = "the letter $c"

    fun beginListening() {
        if (voice.isListening || voice.isSpeaking) return
        if (session.whoseTurn != Speaker.CHILD || session.status != GameStatus.PLAYING) return
        guesses = emptyList()
        childFeedback = null
        voice.startListening(
            onResult = { cands ->
                for (c in cands) {
                    val r = session.submitChild(c)
                    when (r) {
                        is SubmitResult.Accepted -> {
                            guesses = emptyList()
                            childFeedback = null
                            showTyping = false
                            typed = ""
                            celebrateWord = r.word
                            sound.correct()
                            confetti++
                            // Celebrate in composition scope so Lettie's turn effect isn't the owner.
                            scope.launch { voice.speakAwait("Yes! ${r.word.name}!") }
                            return@startListening
                        }
                        is SubmitResult.WrongLetter -> {
                            sound.wrong()
                            childFeedback = "${r.word.name} starts with ${r.word.firstLetter}. Need ${r.required}!"
                            session.consumeRetry()
                            scope.launch {
                                voice.speakAwait(
                                    "${r.word.name} starts with ${r.word.firstLetter}. Try a word starting with ${letterPhrase(r.required)}.",
                                )
                            }
                            return@startListening
                        }
                        is SubmitResult.AlreadyUsed -> {
                            sound.wrong()
                            childFeedback = "Already used ${r.word.name}!"
                            session.consumeRetry()
                            scope.launch { voice.speakAwait("We already used that one. Try another.") }
                            return@startListening
                        }
                        is SubmitResult.NotRecognized -> Unit
                    }
                }
                sound.wrong()
                val g = repo.guesses(cands.firstOrNull().orEmpty(), session.activeCategories)
                guesses = g
                childFeedback = if (g.isEmpty()) "I didn't catch that — try again!" else "Did you mean\u2026?"
                session.consumeRetry()
            },
            onError = { msg ->
                childFeedback = msg
                if (!voice.recognitionAvailable) showTyping = true
            },
        )
    }

    // Fresh start OR resume after Detail (session already begun — don't reset).
    LaunchedEffect(session) {
        if (session.hasBegun) {
            ready = true
            celebrateWord = session.lastWord
            caption = when (session.status) {
                GameStatus.CHILD_WON -> "You won!"
                GameStatus.LETTIE_WON -> "Good try!"
                else -> when (session.whoseTurn) {
                    Speaker.CHILD -> "Your turn! Letter ${session.requiredLetter}"
                    Speaker.LETTIE -> "Lettie's turn\u2026"
                }
            }
            return@LaunchedEffect
        }
        val opener = session.start()
        ready = true
        if (opener != null) {
            caption = "Lettie played ${opener.name}!"
            celebrateWord = opener
            voice.speakAwait(
                "Let's play! My word is ${opener.name}. Your turn! Give me a word starting with ${letterPhrase(session.requiredLetter)}.",
            )
        } else {
            caption = "You start!"
            voice.speakAwait("You start! Give me a word starting with ${letterPhrase(session.requiredLetter)}.")
        }
    }

    // Lettie's turn: speak AFTER play via composition scope so cancelling this
    // effect (because whoseTurn flips to CHILD) does not stopSpeaking().
    LaunchedEffect(session.whoseTurn, session.status, ready) {
        if (!ready) return@LaunchedEffect
        if (session.whoseTurn != Speaker.LETTIE || session.status != GameStatus.PLAYING) return@LaunchedEffect

        voice.awaitSilent()
        delay(300)
        if (session.whoseTurn != Speaker.LETTIE || session.status != GameStatus.PLAYING) return@LaunchedEffect

        caption = "Lettie is thinking\u2026"
        delay(600)
        if (session.whoseTurn != Speaker.LETTIE || session.status != GameStatus.PLAYING) return@LaunchedEffect

        val played = session.lettieTurn()
        // whoseTurn is now CHILD — this LaunchedEffect will cancel. Finish speech off-effect.
        if (played != null) {
            caption = "Lettie played ${played.name}!"
            celebrateWord = played
            lettieJob?.cancel()
            lettieJob = scope.launch {
                voice.speakAwait(
                    "My word is ${played.name}. Your turn! A word starting with ${letterPhrase(session.requiredLetter)}.",
                )
            }
        } else {
            caption = "Lettie is stuck — you win!"
            lettieJob = scope.launch { voice.speakAwait("Oh no! I am stuck. You win!") }
        }
    }

    // Child timer — stored on session so Detail leave/return resumes the same countdown.
    LaunchedEffect(session.whoseTurn, session.status, session.chain.size) {
        if (session.whoseTurn != Speaker.CHILD || session.status != GameStatus.PLAYING) return@LaunchedEffect
        session.armChildTimerIfNeeded()
        voice.awaitSilent()
        while (session.childTimeLeftMs > 0 &&
            session.whoseTurn == Speaker.CHILD &&
            session.status == GameStatus.PLAYING
        ) {
            delay(100)
            if (!paused && !voice.isListening && !voice.isSpeaking) {
                session.consumeChildTimer(100)
            }
        }
        if (session.childTimeLeftMs <= 0 &&
            session.status == GameStatus.PLAYING &&
            session.whoseTurn == Speaker.CHILD
        ) {
            session.childStuck()
            caption = "Time's up! Good try."
            scope.launch { voice.speakAwait("Time is up! Good try!") }
        }
    }

    // Auto-open the mic once per child turn after Lettie finishes speaking.
    LaunchedEffect(session.whoseTurn, session.status, session.chain.size, ready) {
        if (!ready) return@LaunchedEffect
        if (session.whoseTurn != Speaker.CHILD || session.status != GameStatus.PLAYING) return@LaunchedEffect
        if (showTyping) return@LaunchedEffect
        if (autoListenAtChainSize == session.chain.size) return@LaunchedEffect

        voice.awaitSilent()
        delay(350)
        if (session.whoseTurn != Speaker.CHILD || session.status != GameStatus.PLAYING) return@LaunchedEffect
        if (voice.isListening || showTyping) return@LaunchedEffect

        autoListenAtChainSize = session.chain.size
        beginListening()
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
    val featured = celebrateWord ?: session.lastWord
    val micScale by animateFloatAsState(
        targetValue = when {
            voice.isListening -> 1.05f
            childTurn -> 1f
            else -> 0.97f
        },
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "micScale",
    )

    AppScaffold(title = session.round.displayName, onBack = onExit) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp, bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = caption,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(8.dp))

                // Hero: letter first (the job), photo of last word beside/below as context
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Text(
                            text = if (childTurn) "Say a word starting with" else "Next letter",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                        )
                        AnimatedContent(
                            targetState = session.requiredLetter,
                            transitionSpec = {
                                (fadeIn() + scaleIn(initialScale = 0.5f)) togetherWith fadeOut()
                            },
                            label = "requiredLetter",
                        ) { letter ->
                            Text(
                                text = if (letter == ' ') "?" else letter.toString(),
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 96.sp),
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        if (childTurn) {
                            LinearProgressIndicator(
                                progress = { (session.childTimeLeftMs.toFloat() / totalMs).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(10.dp),
                            )
                            Text(
                                "${(session.childTimeLeftMs + 999) / 1000} seconds left",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Last word photo — supporting context, not competing with the letter
                AnimatedContent(
                    targetState = featured?.id,
                    transitionSpec = {
                        (fadeIn(tween(250)) + scaleIn(initialScale = 0.92f)) togetherWith
                            (fadeOut(tween(150)) + scaleOut(targetScale = 0.96f))
                    },
                    label = "featuredWord",
                    modifier = Modifier.fillMaxWidth(),
                ) { featuredId ->
                    val word = featured.takeIf { it?.id == featuredId }
                    if (word != null) {
                        Card(
                            onClick = { onOpenWord(word) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                WordImage(word = word, size = 72.dp, emojiFallbackSize = 40.sp)
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        word.name,
                                        style = MaterialTheme.typography.headlineSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        text = when {
                                            session.chain.lastOrNull()?.word?.id == word.id &&
                                                session.chain.lastOrNull()?.speaker == Speaker.LETTIE ->
                                                "Lettie\u2019s word"
                                            session.chain.lastOrNull()?.word?.id == word.id ->
                                                "You said it!"
                                            else -> word.category.displayName
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    )
                                }
                            }
                        }
                    }
                }

                if (session.chain.size > 1) {
                    Spacer(Modifier.height(8.dp))
                    WordChain(session.chain, onOpenWord = onOpenWord)
                }

                AnimatedVisibility(visible = childFeedback != null) {
                    childFeedback?.let { msg ->
                        Text(
                            msg,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        )
                    }
                }

                if (guesses.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(guesses, key = { it.id }) { word ->
                            AssistChip(
                                onClick = {
                                    val r = session.submitConfirmed(word)
                                    if (r is SubmitResult.Accepted) {
                                        celebrateWord = r.word
                                        sound.correct()
                                        confetti++
                                        guesses = emptyList()
                                        childFeedback = null
                                        scope.launch { voice.speakAwait("Yes! ${r.word.name}!") }
                                    }
                                },
                                leadingIcon = {
                                    WordImage(word, size = 28.dp, emojiFallbackSize = 16.sp)
                                },
                                label = { Text(word.name) },
                            )
                        }
                    }
                }

                if (showTyping) {
                    Spacer(Modifier.height(8.dp))
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
                        Button(
                            onClick = {
                                if (typed.isBlank()) return@Button
                                val r = session.submitChild(typed)
                                when (r) {
                                    is SubmitResult.Accepted -> {
                                        celebrateWord = r.word
                                        sound.correct()
                                        confetti++
                                        showTyping = false
                                        typed = ""
                                        childFeedback = null
                                        guesses = emptyList()
                                        scope.launch { voice.speakAwait("Yes! ${r.word.name}!") }
                                    }
                                    is SubmitResult.WrongLetter -> {
                                        sound.wrong()
                                        childFeedback = "${r.word.name} starts with ${r.word.firstLetter}. Need ${r.required}!"
                                        session.consumeRetry()
                                    }
                                    is SubmitResult.AlreadyUsed -> {
                                        sound.wrong()
                                        childFeedback = "Already used ${r.word.name}!"
                                        session.consumeRetry()
                                    }
                                    is SubmitResult.NotRecognized -> {
                                        sound.wrong()
                                        guesses = r.guesses
                                        childFeedback = if (r.guesses.isEmpty()) {
                                            "I don't know that word."
                                        } else {
                                            "Did you mean\u2026?"
                                        }
                                        session.consumeRetry()
                                    }
                                }
                            },
                        ) { Text("Go") }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { beginListening() },
                    enabled = childTurn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .scale(micScale),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (voice.isListening) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                    ),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_mic),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = when {
                            voice.isListening -> "Listening\u2026 speak now!"
                            childTurn -> "Tap & Speak"
                            else -> "Wait for Lettie\u2026"
                        },
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FilledTonalButton(
                        onClick = {
                            val hint = session.hintWord()
                            childFeedback = if (hint != null) {
                                scope.launch {
                                    voice.speakAwait(
                                        "Here's a hint. Try a word starting with ${letterPhrase(session.requiredLetter)}, like ${hint.name}.",
                                    )
                                }
                                "Try ${hint.name}!"
                            } else {
                                "Tricky letter!"
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_lightbulb),
                            contentDescription = null,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Hint")
                    }
                    FilledTonalButton(
                        onClick = { showTyping = !showTyping },
                        modifier = Modifier.weight(1f).height(48.dp),
                    ) {
                        Text(if (showTyping) "Hide keyboard" else "Type instead")
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
                    autoListenAtChainSize = -1
                    scope.launch {
                        val opener = session.restart()
                        ready = true
                        if (opener != null) {
                            caption = "Lettie played ${opener.name}!"
                            celebrateWord = opener
                            voice.speakAwait("Let's play again! My word is ${opener.name}. Your turn!")
                        } else {
                            caption = "You start!"
                            celebrateWord = null
                            voice.speakAwait("Let's play again! You start!")
                        }
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
    val listState = rememberLazyListState()
    LaunchedEffect(chain.size) {
        if (chain.isNotEmpty()) listState.animateScrollToItem(chain.lastIndex)
    }
    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(chain.size) { index ->
            val entry = chain[index]
            AssistChip(
                onClick = { onOpenWord(entry.word) },
                leadingIcon = {
                    WordImage(entry.word, size = 24.dp, emojiFallbackSize = 14.sp)
                },
                label = {
                    Text(
                        "${if (entry.speaker == Speaker.LETTIE) "L" else "You"}: ${entry.word.name}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
        }
    }
}
