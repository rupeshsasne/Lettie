package com.radix2.llm.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radix2.llm.data.FavoritesStore
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Word
import com.radix2.llm.voice.VoiceController
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.ic_favorite
import lastlettermaster.shared.generated.resources.ic_favorite_border
import lastlettermaster.shared.generated.resources.ic_volume_up
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    repo: WordRepository,
    voice: VoiceController,
    favorites: FavoritesStore,
    initialCategory: Category?,
    onOpenWord: (Word) -> Unit,
    onBack: () -> Unit,
) {
    var selectedName by rememberSaveable {
        mutableStateOf(initialCategory?.name)
    }
    var showFavorites by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    val searchListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    val selected = selectedName?.let { name ->
        Category.entries.find { it.name == name }
    }
    val categories = if (selected == null) Category.entries.toList() else listOf(selected)
    val trimmed = query.trim()

    AppScaffold(title = "Words", onBack = onBack) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                singleLine = true,
                label = { Text("Search words") },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) { Text("\u2715") }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    FilterChip(
                        selected = showFavorites,
                        onClick = { showFavorites = !showFavorites },
                        label = { Text("\u2764 Favorites") },
                    )
                }
                item {
                    FilterChip(
                        selected = selected == null && !showFavorites,
                        onClick = { selectedName = null; showFavorites = false },
                        label = { Text("All") },
                    )
                }
                items(Category.entries.toList()) { cat ->
                    FilterChip(
                        selected = selected == cat,
                        onClick = { selectedName = cat.name; showFavorites = false },
                        label = { Text(cat.displayName) },
                    )
                }
            }

            val filtered: List<Word> = when {
                trimmed.isNotEmpty() -> repo.search(trimmed, categories)
                    .filter { !showFavorites || favorites.isFavorite(it.id) }
                showFavorites -> repo.all
                    .filter { it.category in categories && favorites.isFavorite(it.id) }
                    .sortedBy { it.name.lowercase() }
                else -> emptyList()
            }

            if (trimmed.isNotEmpty() || showFavorites) {
                if (filtered.isEmpty()) {
                    EmptyHint(
                        if (showFavorites && trimmed.isEmpty()) {
                            "No favorites yet. Tap the \u2764 on any word to save it."
                        } else {
                            "No words found for \"$trimmed\"."
                        },
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize(), state = searchListState) {
                        items(filtered, key = { it.id }) { word ->
                            WordRow(word, favorites, voice, onOpenWord)
                            HorizontalDivider()
                        }
                    }
                }
            } else {
                val sections = remember(selectedName) { repo.alphabetical(categories) }
                LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                    sections.forEach { (letter, words) ->
                        item(key = "header_$letter") {
                            Text(
                                text = letter.toString(),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            )
                        }
                        items(words, key = { it.id }) { word ->
                            WordRow(word, favorites, voice, onOpenWord)
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WordRow(
    word: Word,
    favorites: FavoritesStore,
    voice: VoiceController,
    onOpenWord: (Word) -> Unit,
) {
    val isFav = favorites.isFavorite(word.id)
    ListItem(
        headlineContent = { Text(word.name) },
        supportingContent = { Text(word.category.displayName) },
        leadingContent = {
            WordImage(word = word, size = 48.dp, emojiFallbackSize = 28.sp)
        },
        trailingContent = {
            Row {
                IconButton(onClick = { favorites.toggle(word.id) }) {
                    Icon(
                        painter = painterResource(
                            if (isFav) Res.drawable.ic_favorite else Res.drawable.ic_favorite_border,
                        ),
                        contentDescription = if (isFav) "Remove favorite" else "Add favorite",
                        tint = if (isFav) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { voice.speak(word.name) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_volume_up),
                        contentDescription = "Hear ${word.name}",
                    )
                }
            }
        },
        modifier = Modifier.clickable { onOpenWord(word) },
    )
}

@Composable
private fun EmptyHint(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth().padding(24.dp),
    )
}
