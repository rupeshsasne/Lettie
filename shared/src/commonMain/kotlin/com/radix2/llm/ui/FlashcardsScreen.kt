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
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
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
    var category by remember { mutableStateOf<Category?>(null) }
    val deck = remember(category) {
        val cats = category?.let { listOf(it) } ?: Category.entries.toList()
        repo.all.filter { it.category in cats }.shuffled()
    }

    var index by remember(category) { mutableStateOf(0) }
    var flipped by remember(category) { mutableStateOf(false) }

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
                        selected = category == null,
                        onClick = { category = null },
                        label = { Text("All") },
                    )
                }
                items(Category.entries.toList()) { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat.displayName) },
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
                    .height(320.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    }
                    .clickable { flipped = !flipped },
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (!showingBack) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            WordImage(card, size = 160.dp, emojiFallbackSize = 96.sp)
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
                            modifier = Modifier.graphicsLayer { rotationY = 180f },
                        ) {
                            Text(
                                card.name,
                                style = MaterialTheme.typography.displaySmall,
                                textAlign = TextAlign.Center,
                            )
                            card.syllables?.let {
                                Spacer(Modifier.height(8.dp))
                                Text(it, style = MaterialTheme.typography.titleMedium)
                            }
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
