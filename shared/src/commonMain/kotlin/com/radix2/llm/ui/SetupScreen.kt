package com.radix2.llm.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Difficulty
import com.radix2.llm.domain.Round

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    repo: WordRepository,
    onStart: (Round, List<Category>, Difficulty) -> Unit,
    onBack: () -> Unit,
) {
    var round by remember { mutableStateOf(Round.ROUND_1) }
    var focusCategory by remember { mutableStateOf<Category?>(null) }
    var difficulty by remember { mutableStateOf(Difficulty.EASY) }

    AppScaffold(title = "New Game", onBack = onBack) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            SectionLabel("Round")
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                Round.entries.forEachIndexed { index, r ->
                    SegmentedButton(
                        selected = round == r,
                        onClick = {
                            round = r
                            focusCategory = null
                        },
                        shape = SegmentedButtonDefaults.itemShape(index, Round.entries.size),
                    ) {
                        Text(r.displayName)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionLabel("What to practice")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = focusCategory == null,
                    onClick = { focusCategory = null },
                    label = { Text("Whole round") },
                )
                round.categories.forEach { cat ->
                    FilterChip(
                        selected = focusCategory == cat,
                        onClick = { focusCategory = cat },
                        label = { Text(cat.displayName) },
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionLabel("Difficulty")
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                Difficulty.entries.forEachIndexed { index, d ->
                    SegmentedButton(
                        selected = difficulty == d,
                        onClick = { difficulty = d },
                        shape = SegmentedButtonDefaults.itemShape(index, Difficulty.entries.size),
                    ) {
                        Text(d.label)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Turn timer: ${difficulty.turnSeconds} seconds",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(32.dp))
            Button(
                onClick = {
                    val cats = focusCategory?.let { listOf(it) } ?: round.categories
                    onStart(round, cats, difficulty)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Start game")
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}
