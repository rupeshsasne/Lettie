package com.radix2.llm.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import com.radix2.llm.data.ProgressStore
import com.radix2.llm.data.WordRepository
import com.radix2.llm.game.QuizGenerator
import com.radix2.llm.game.QuizQuestion
import com.radix2.llm.sound.SoundPlayer
import kotlinx.coroutines.delay

private const val QUESTION_COUNT = 8
private const val QUESTION_SECONDS = 15

@Composable
fun QuizScreen(
    repo: WordRepository,
    sound: SoundPlayer,
    progress: ProgressStore,
    onExit: () -> Unit,
) {
    var attempt by remember { mutableStateOf(0) }
    val questions = remember(attempt) { QuizGenerator.generate(repo, QUESTION_COUNT) }

    var index by remember(attempt) { mutableStateOf(0) }
    var score by remember(attempt) { mutableStateOf(0) }
    var selectedId by remember(attempt) { mutableStateOf<String?>(null) }
    var finished by remember(attempt) { mutableStateOf(false) }
    val totalMs = QUESTION_SECONDS * 1000
    var timeLeftMs by remember(attempt) { mutableStateOf(totalMs) }

    val question: QuizQuestion? = questions.getOrNull(index)

    fun advance() {
        if (index + 1 >= questions.size) {
            finished = true
            progress.recordQuiz(score)
        } else {
            index++
            selectedId = null
            timeLeftMs = totalMs
        }
    }

    fun answer(id: String?) {
        if (selectedId != null) return
        selectedId = id ?: "__timeout__"
        val correct = id != null && id == question?.answerId
        if (correct) {
            score++
            sound.correct()
        } else {
            sound.wrong()
        }
    }

    // Per-question countdown.
    LaunchedEffect(index, attempt, selectedId) {
        if (question != null && selectedId == null && !finished) {
            timeLeftMs = totalMs
            while (timeLeftMs > 0 && selectedId == null) {
                delay(100)
                timeLeftMs -= 100
            }
            if (selectedId == null) answer(null)
        }
    }

    // Auto-advance shortly after an answer is shown.
    LaunchedEffect(selectedId) {
        if (selectedId != null && !finished) {
            delay(1100)
            advance()
        }
    }

    AppScaffold(title = "Quiz", onBack = onExit) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when {
                finished || question == null -> QuizResult(
                    score = score,
                    total = questions.size,
                    onPlayAgain = { attempt++ },
                    onHome = onExit,
                )

                else -> {
                    Text(
                        "Question ${index + 1} of ${questions.size}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Score: $score", style = MaterialTheme.typography.titleMedium)

                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { (timeLeftMs.toFloat() / totalMs).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(20.dp))
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Text(
                            question.prompt,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                    question.options.forEach { option ->
                        val revealed = selectedId != null
                        val isAnswer = option.id == question.answerId
                        val isChosen = option.id == selectedId
                        val container = when {
                            !revealed -> MaterialTheme.colorScheme.primaryContainer
                            isAnswer -> MaterialTheme.colorScheme.tertiaryContainer
                            isChosen -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceContainerHighest
                        }
                        Button(
                            onClick = { answer(option.id) },
                            enabled = !revealed,
                            colors = ButtonDefaults.buttonColors(containerColor = container),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .height(64.dp),
                        ) {
                            Text(option.emoji, fontSize = 28.sp)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                option.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizResult(
    score: Int,
    total: Int,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
) {
    val stars = when {
        total == 0 -> 0
        score >= total -> 3
        score >= total * 2 / 3 -> 2
        score >= total / 3 -> 1
        else -> 0
    }
    Spacer(Modifier.height(24.dp))
    Text("All done!", style = MaterialTheme.typography.headlineMedium)
    Spacer(Modifier.height(8.dp))
    Text(
        buildString {
            repeat(stars) { append("\u2B50") }
            repeat(3 - stars) { append("\u2606") }
        },
        fontSize = 48.sp,
    )
    Spacer(Modifier.height(8.dp))
    Text(
        "You got $score out of $total right.",
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
    )
    Spacer(Modifier.height(32.dp))
    Button(onClick = onPlayAgain, modifier = Modifier.fillMaxWidth().height(56.dp)) {
        Text("Play again", style = MaterialTheme.typography.titleMedium)
    }
    Spacer(Modifier.height(12.dp))
    Button(
        onClick = onHome,
        colors = ButtonDefaults.filledTonalButtonColors(),
        modifier = Modifier.fillMaxWidth().height(56.dp),
    ) {
        Text("Home", style = MaterialTheme.typography.titleMedium)
    }
}
