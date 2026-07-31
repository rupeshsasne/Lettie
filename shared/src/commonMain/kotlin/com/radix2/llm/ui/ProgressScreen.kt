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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.radix2.llm.data.ProgressStore
import com.radix2.llm.ui.adaptive.AdaptiveSplit
import com.radix2.llm.ui.adaptive.LettieDestination
import com.radix2.llm.ui.adaptive.LocalWindowSize
import com.radix2.llm.ui.adaptive.MaxWidthContainer
import com.radix2.llm.ui.theme.LettieAtmosphere
import com.radix2.llm.ui.theme.LettieDimens

/**
 * Progress dashboard — streak hero + supporting metrics.
 * Landscape: side-by-side so nothing clips on short height.
 */
@Composable
fun ProgressScreen(
    progress: ProgressStore,
    onExit: () -> Unit,
    selectedDestination: LettieDestination? = LettieDestination.Me,
    onDestinationSelected: ((LettieDestination) -> Unit)? = null,
) {
    var confirmReset by remember { mutableStateOf(false) }

    val winRate = if (progress.gamesPlayed == 0) 0
    else (progress.gamesWon * 100) / progress.gamesPlayed
    val landscape = LocalWindowSize.current.isLandscape

    val content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit = { innerPadding ->
        LettieAtmosphere(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            MaxWidthContainer(modifier = Modifier.fillMaxSize()) {
                if (landscape) {
                    AdaptiveSplit(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(LettieDimens.screenPadding),
                        dualPane = true,
                        startWeight = 0.42f,
                        endWeight = 0.58f,
                        start = {
                            StreakHero(
                                streak = progress.currentStreak,
                                best = progress.bestStreak,
                                compact = true,
                                modifier = Modifier.fillMaxSize(),
                            )
                        },
                        end = {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(start = LettieDimens.spaceSm),
                            ) {
                                Text("You", style = MaterialTheme.typography.headlineSmall)
                                Text(
                                    "Keep the streak glowing.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 2.dp, bottom = LettieDimens.spaceMd),
                                )
                                MetricsList(
                                    gamesPlayed = progress.gamesPlayed,
                                    winsLabel = "${progress.gamesWon}  ·  $winRate%",
                                    bestChain = progress.bestChain,
                                    wordsLearned = progress.wordsLearnedCount,
                                    quizBest = progress.quizBest,
                                )
                                TextButton(
                                    onClick = { confirmReset = true },
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(top = LettieDimens.spaceMd),
                                ) {
                                    Text("Reset progress", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        },
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(LettieDimens.screenPadding),
                    ) {
                        Text("You", style = MaterialTheme.typography.headlineMedium)
                        Text(
                            "Keep the streak glowing.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = LettieDimens.spaceLg),
                        )
                        StreakHero(
                            streak = progress.currentStreak,
                            best = progress.bestStreak,
                            compact = false,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(LettieDimens.spaceLg))
                        Text(
                            "Your play",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = LettieDimens.spaceSm),
                        )
                        MetricsList(
                            gamesPlayed = progress.gamesPlayed,
                            winsLabel = "${progress.gamesWon}  ·  $winRate%",
                            bestChain = progress.bestChain,
                            wordsLearned = progress.wordsLearnedCount,
                            quizBest = progress.quizBest,
                        )
                        Spacer(Modifier.height(LettieDimens.spaceLg))
                        TextButton(
                            onClick = { confirmReset = true },
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        ) {
                            Text("Reset progress", color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(Modifier.height(LettieDimens.spaceMd))
                    }
                }
            }
        }
    }

    if (selectedDestination != null && onDestinationSelected != null) {
        MainScaffold(
            selected = selectedDestination,
            onDestinationSelected = onDestinationSelected,
            title = null,
            content = content,
        )
    } else {
        AppScaffold(title = "My Progress", onBack = onExit, content = content)
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

@Composable
private fun StreakHero(
    streak: Int,
    best: Int,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp,
    ) {
        if (compact) {
            // Horizontal composition for short landscape height — fits without clipping.
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(LettieDimens.spaceLg),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LettieDimens.spaceMd),
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.55f),
                    modifier = Modifier.size(64.dp),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("🔥", style = MaterialTheme.typography.headlineMedium)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "$streak",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        "day streak",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        "Best ever: $best",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LettieDimens.spaceXl),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.55f),
                    modifier = Modifier.size(72.dp),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("🔥", style = MaterialTheme.typography.headlineLarge)
                    }
                }
                Spacer(Modifier.height(LettieDimens.spaceMd))
                Text(
                    "$streak",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    "day streak",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    "Best ever: $best",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun MetricsList(
    gamesPlayed: Int,
    winsLabel: String,
    bestChain: Int,
    wordsLearned: Int,
    quizBest: Int,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column {
            MetricRow("Games played", gamesPlayed.toString())
            DividerQuiet()
            MetricRow("Wins", winsLabel)
            DividerQuiet()
            MetricRow("Longest chain", bestChain.toString())
            DividerQuiet()
            MetricRow("Words learned", wordsLearned.toString())
            DividerQuiet()
            MetricRow("Best quiz", quizBest.toString())
        }
    }
}

@Composable
private fun MetricRow(label: String, value: String) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = {
            Text(label, style = MaterialTheme.typography.bodyLarge)
        },
        trailingContent = {
            Text(
                value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End,
            )
        },
    )
}

@Composable
private fun DividerQuiet() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
    )
}
