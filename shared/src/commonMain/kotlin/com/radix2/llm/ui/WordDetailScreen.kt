package com.radix2.llm.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radix2.llm.data.FavoritesStore
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.KidFacts
import com.radix2.llm.domain.Pronunciation
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.Word
import com.radix2.llm.voice.VoiceController
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.ic_favorite
import lastlettermaster.shared.generated.resources.ic_favorite_border
import lastlettermaster.shared.generated.resources.ic_volume_up
import org.jetbrains.compose.resources.painterResource

@Composable
fun WordDetailScreen(
    word: Word,
    repo: WordRepository,
    favorites: FavoritesStore,
    voice: VoiceController,
    onOpenWord: (Word) -> Unit,
    onBack: () -> Unit,
) {
    val isFav = favorites.isFavorite(word.id)
    val round = remember(word.id) {
        Round.entries.firstOrNull { word.category in it.categories }
    }
    val nextWords = remember(word.id) {
        val roundCats = round?.categories ?: listOf(word.category)
        repo.startingWith(word.lastLetter, roundCats, setOf(word.id)).take(8)
    }
    val facts = remember(word.id) { KidFacts.forWord(word) }
    val sayItLike = remember(word.id) { Pronunciation.sayItLike(word) }

    AppScaffold(
        title = word.name,
        onBack = onBack,
        actions = {
            IconButton(onClick = { favorites.toggle(word.id) }) {
                Icon(
                    painter = painterResource(
                        if (isFav) Res.drawable.ic_favorite else Res.drawable.ic_favorite_border,
                    ),
                    contentDescription = if (isFav) "Remove favorite" else "Add favorite",
                    tint = if (isFav) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    WordImage(
                        word = word,
                        size = 200.dp,
                        emojiFallbackSize = 112.sp,
                    )
                }
            }

            FilledTonalButton(
                onClick = { voice.speak(word.name) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_volume_up),
                    contentDescription = null,
                )
                Spacer(Modifier.width(8.dp))
                Text("Hear the word", style = MaterialTheme.typography.titleMedium)
            }

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    InfoItem("Category", word.category.displayName)
                    HorizontalDivider()
                    round?.let {
                        InfoItem("Round", it.displayName)
                        HorizontalDivider()
                    }
                    InfoItem("Level", word.difficulty.label)
                    HorizontalDivider()
                    InfoItem("Starts with", word.firstLetter.toString())
                    HorizontalDivider()
                    InfoItem("Ends with", word.lastLetter.toString())
                    HorizontalDivider()
                    InfoItem("Say it like", sayItLike)
                    if (word.aliases.isNotEmpty()) {
                        HorizontalDivider()
                        InfoItem("Also called", word.aliases.joinToString(", "))
                    }
                    word.geo?.capital
                        ?.takeIf { it.isNotBlank() && it != "Capital" && it != "—" }
                        ?.let {
                            HorizontalDivider()
                            InfoItem("Capital", it)
                        }
                    word.geo?.countryOf
                        ?.takeIf { it.isNotBlank() && it != "World" }
                        ?.let {
                            HorizontalDivider()
                            InfoItem("Country", it)
                        }
                }
            }

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("About ${word.name}", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = facts[0],
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                    Text(
                        text = facts[1],
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }

            if (nextWords.isNotEmpty()) {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Words you could play next (start with ${word.lastLetter})",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(Modifier.height(12.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(nextWords, key = { it.id }) { next ->
                                AssistChip(
                                    onClick = { onOpenWord(next) },
                                    leadingIcon = { Text(next.emoji) },
                                    label = { Text(next.name) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = { Text(label) },
        trailingContent = {
            Text(value, style = MaterialTheme.typography.titleMedium)
        },
    )
}
