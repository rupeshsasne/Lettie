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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Difficulty
import com.radix2.llm.domain.Round
import com.radix2.llm.ui.adaptive.AdaptiveSplit
import com.radix2.llm.ui.adaptive.LocalWindowSize
import com.radix2.llm.ui.adaptive.MaxWidthContainer
import com.radix2.llm.ui.theme.LettieDimens

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
    val landscape = LocalWindowSize.current.isLandscape

    AppScaffold(title = "New Game", onBack = onBack) { innerPadding ->
        MaxWidthContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (landscape) {
                AdaptiveSplit(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(LettieDimens.screenPadding),
                    dualPane = true,
                    startWeight = 0.62f,
                    endWeight = 0.38f,
                    start = {
                        SetupForm(
                            round = round,
                            onRound = { round = it; focusCategory = null },
                            focusCategory = focusCategory,
                            onFocusCategory = { focusCategory = it },
                            difficulty = difficulty,
                            onDifficulty = { difficulty = it },
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                        )
                    },
                    end = {
                        SetupLaunchCard(
                            round = round,
                            focusCategory = focusCategory,
                            difficulty = difficulty,
                            onStart = {
                                val cats = focusCategory?.let { listOf(it) } ?: round.categories
                                onStart(round, cats, difficulty)
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    },
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = LettieDimens.screenPadding)
                        .padding(vertical = LettieDimens.spaceMd),
                ) {
                    SetupForm(
                        round = round,
                        onRound = { round = it; focusCategory = null },
                        focusCategory = focusCategory,
                        onFocusCategory = { focusCategory = it },
                        difficulty = difficulty,
                        onDifficulty = { difficulty = it },
                    )
                    Spacer(Modifier.height(LettieDimens.spaceXl))
                    Button(
                        onClick = {
                            val cats = focusCategory?.let { listOf(it) } ?: round.categories
                            onStart(round, cats, difficulty)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(LettieDimens.primaryCtaHeight),
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Text("Start game", style = MaterialTheme.typography.titleLarge)
                    }
                    Spacer(Modifier.height(LettieDimens.spaceLg))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SetupForm(
    round: Round,
    onRound: (Round) -> Unit,
    focusCategory: Category?,
    onFocusCategory: (Category?) -> Unit,
    difficulty: Difficulty,
    onDifficulty: (Difficulty) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            "Ready when you are",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            "Pick a round, what to practice, and how fast.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = LettieDimens.spaceLg),
        )

        SectionLabel("Round")
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            Round.entries.forEachIndexed { index, r ->
                SegmentedButton(
                    selected = round == r,
                    onClick = { onRound(r) },
                    shape = SegmentedButtonDefaults.itemShape(index, Round.entries.size),
                ) {
                    Text(r.displayName)
                }
            }
        }

        Spacer(Modifier.height(LettieDimens.spaceLg))
        SectionLabel("What to practice")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = focusCategory == null,
                onClick = { onFocusCategory(null) },
                label = { Text("Whole round") },
            )
            round.categories.forEach { cat ->
                FilterChip(
                    selected = focusCategory == cat,
                    onClick = { onFocusCategory(cat) },
                    label = { Text(cat.displayName) },
                )
            }
        }

        Spacer(Modifier.height(LettieDimens.spaceLg))
        SectionLabel("Difficulty")
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            Difficulty.entries.forEachIndexed { index, d ->
                SegmentedButton(
                    selected = difficulty == d,
                    onClick = { onDifficulty(d) },
                    shape = SegmentedButtonDefaults.itemShape(index, Difficulty.entries.size),
                ) {
                    Text(d.label)
                }
            }
        }
        Text(
            "Turn timer: ${difficulty.turnSeconds} seconds",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun SetupLaunchCard(
    round: Round,
    focusCategory: Category?,
    difficulty: Difficulty,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(LettieDimens.spaceLg),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Your match",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(Modifier.height(LettieDimens.spaceMd))
                Text(
                    round.displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    focusCategory?.displayName ?: "Whole round",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                )
                Text(
                    difficulty.label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LettieDimens.primaryCtaHeight),
                shape = MaterialTheme.shapes.large,
            ) {
                Text("Start game", style = MaterialTheme.typography.titleLarge)
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
