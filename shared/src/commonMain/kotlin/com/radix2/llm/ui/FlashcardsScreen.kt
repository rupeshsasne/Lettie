package com.radix2.llm.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radix2.llm.data.LetterCoverage
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Pronunciation
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.Word
import com.radix2.llm.voice.VoiceController
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.ic_volume_up
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardsScreen(
    repo: WordRepository,
    voice: VoiceController,
    onExit: () -> Unit,
) {
    var selectedRoundNames by remember { mutableStateOf<List<String>>(emptyList()) }
    val categories = remember(selectedRoundNames) {
        val rounds = selectedRoundNames.mapNotNull { name ->
            Round.entries.find { it.name == name }
        }
        if (rounds.isEmpty()) Category.entries.toList()
        else rounds.flatMap { it.categories }.distinct()
    }
    val deck = remember(selectedRoundNames) {
        pedagogicalDeck(repo, categories)
    }

    var index by remember(selectedRoundNames) { mutableStateOf(0) }
    var flipped by remember(selectedRoundNames) { mutableStateOf(false) }

    val card = deck.getOrNull(index)
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(400),
        label = "flip",
    )

    AppScaffold(title = "Flashcards", onBack = onExit) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyRow(
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    FilterChip(
                        selected = selectedRoundNames.isEmpty(),
                        onClick = { selectedRoundNames = emptyList() },
                        label = { Text("All") },
                    )
                }
                items(Round.entries.toList()) { round ->
                    val selected = round.name in selectedRoundNames
                    FilterChip(
                        selected = selected,
                        onClick = {
                            selectedRoundNames = if (selected) {
                                selectedRoundNames - round.name
                            } else {
                                selectedRoundNames + round.name
                            }
                        },
                        label = { Text(round.displayName) },
                    )
                }
            }

            if (card == null) {
                Spacer(Modifier.height(48.dp))
                Text("No cards here yet.", style = MaterialTheme.typography.titleMedium)
                return@Column
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Card ${index + 1} of ${deck.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))

            val showingBack = rotation > 90f
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    }
                    .clickable { flipped = !flipped },
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (!showingBack) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            WordImage(card, size = 220.dp, emojiFallbackSize = 128.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Tap to reveal",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    } else {
                        // Counter-rotate so the back reads normally.
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .graphicsLayer { rotationY = 180f },
                        ) {
                            Text(
                                card.name,
                                style = MaterialTheme.typography.displaySmall,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                Pronunciation.sayItLike(card),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                card.category.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.height(16.dp))
                            FilledTonalButton(onClick = { voice.speak(card.name) }) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_volume_up),
                                    contentDescription = null,
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Hear it")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = { if (index > 0) { index--; flipped = false } },
                    enabled = index > 0,
                    modifier = Modifier.weight(1f).height(56.dp),
                ) { Text("Previous") }
                FilledTonalButton(
                    onClick = { if (index < deck.size - 1) { index++; flipped = false } },
                    enabled = index < deck.size - 1,
                    modifier = Modifier.weight(1f).height(56.dp),
                ) { Text("Next") }
            }
        }
    }
}

/** Practice jammed keys first: letters that often end words, then Easy→Hard, stable ids. */
private fun pedagogicalDeck(
    repo: WordRepository,
    categories: List<Category>,
): List<Word> {
    val words = repo.all.filter { it.category in categories }
    val rounds = Round.entries.filter { round -> round.categories.any { it in categories } }
        .ifEmpty { Round.entries.toList() }
    val endingPriority = rounds.flatMap { LetterCoverage.topEndings(it) }.distinct()
    val freq = LetterCoverage.endingFrequency(words).associate { it.first to it.second }

    return words.sortedWith(
        compareBy<Word>(
            { word ->
                val letter = word.firstLetter.uppercaseChar()
                val idx = endingPriority.indexOf(letter)
                if (idx >= 0) idx else endingPriority.size + 26 - (freq[letter] ?: 0)
            },
            { it.difficulty.ordinal },
            { it.id },
        ),
    )
}
