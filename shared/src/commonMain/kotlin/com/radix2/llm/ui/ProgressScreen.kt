package com.radix2.llm.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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

@Composable
fun ProgressScreen(
    progress: ProgressStore,
    onExit: () -> Unit,
) {
    var confirmReset by remember { mutableStateOf(false) }

    val winRate = if (progress.gamesPlayed == 0) 0
    else (progress.gamesWon * 100) / progress.gamesPlayed

    AppScaffold(title = "My Progress", onBack = onExit) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatRow(
                StatData("\uD83C\uDFAE", "Games played", progress.gamesPlayed.toString()),
                StatData("\uD83C\uDFC6", "Wins", "${progress.gamesWon}  (${winRate}%)"),
            )
            StatRow(
                StatData("\uD83D\uDD25", "Day streak", progress.currentStreak.toString()),
                StatData("\u2B50", "Best streak", progress.bestStreak.toString()),
            )
            StatRow(
                StatData("\uD83D\uDD17", "Longest chain", progress.bestChain.toString()),
                StatData("\uD83D\uDCDA", "Words learned", progress.wordsLearnedCount.toString()),
            )
            StatRow(
                StatData("\u2753", "Best quiz score", progress.quizBest.toString()),
                null,
            )

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { confirmReset = true },
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text("Reset progress")
            }
        }
    }

    if (confirmReset) {
        AlertDialog(
            onDismissRequest = { confirmReset = false },
            title = { Text("Reset progress?") },
            text = { Text("This clears all stats, streaks and learned words. This can't be undone.") },
            confirmButton = {
                TextButton(onClick = { progress.reset(); confirmReset = false }) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { confirmReset = false }) { Text("Cancel") }
            },
        )
    }
}

private data class StatData(val emoji: String, val label: String, val value: String)

@Composable
private fun StatRow(left: StatData, right: StatData?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(left, Modifier.weight(1f))
        if (right != null) {
            StatCard(right, Modifier.weight(1f))
        } else {
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(data: StatData, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier.height(120.dp)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(data.emoji, fontSize = 30.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                data.value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                data.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
