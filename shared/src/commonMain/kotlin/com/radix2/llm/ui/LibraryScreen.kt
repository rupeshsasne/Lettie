package com.radix2.llm.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radix2.llm.data.FavoritesStore
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.Word
import com.radix2.llm.voice.VoiceController
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.ic_favorite
import lastlettermaster.shared.generated.resources.ic_favorite_border
import lastlettermaster.shared.generated.resources.ic_volume_up
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs
import kotlin.math.roundToInt

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
    var selectedRoundNames by rememberSaveable {
        mutableStateOf(
            initialCategory?.let { cat ->
                Round.entries.find { cat in it.categories }?.let { listOf(it.name) }
            } ?: emptyList(),
        )
    }
    var showFavorites by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    val searchListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    val categories = remember(selectedRoundNames) {
        val rounds = selectedRoundNames.mapNotNull { name ->
            Round.entries.find { it.name == name }
        }
        if (rounds.isEmpty()) Category.entries.toList()
        else rounds.flatMap { it.categories }.distinct()
    }
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
                val sections = remember(selectedRoundNames) { repo.alphabetical(categories) }
                val letterRanges = remember(sections) {
                    var index = 0
                    buildList {
                        sections.forEach { (letter, words) ->
                            val start = index
                            index += 1 + words.size
                            add(LetterRange(letter, start, index))
                        }
                    }
                }
                val letterOffsets = remember(letterRanges) {
                    letterRanges.associate { it.letter to it.startIndex }
                }
                val availableLetters = remember(sections) { sections.keys }
                val scope = rememberCoroutineScope()
                var scrollJob by remember { mutableStateOf<Job?>(null) }
                var scrubLetter by remember { mutableStateOf<Char?>(null) }

                val activeFromScroll by remember(letterRanges) {
                    derivedStateOf {
                        val idx = listState.firstVisibleItemIndex
                        letterRanges.lastOrNull { idx >= it.startIndex }?.letter
                            ?: letterRanges.firstOrNull()?.letter
                    }
                }
                val activeLetter = scrubLetter ?: activeFromScroll

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 48.dp),
                        state = listState,
                    ) {
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

                    ElasticAlphabetStrip(
                        available = availableLetters,
                        activeLetter = activeLetter,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .padding(vertical = 4.dp),
                        onJumpLetter = { letter ->
                            val target = letterOffsets[letter] ?: return@ElasticAlphabetStrip
                            scrubLetter = letter
                            scrollJob?.cancel()
                            scrollJob = scope.launch {
                                try {
                                    listState.smoothScrollToIndex(target)
                                } finally {
                                    scrubLetter = null
                                }
                            }
                        },
                        onScrubLetter = { letter ->
                            val target = letterOffsets[letter] ?: return@ElasticAlphabetStrip
                            if (scrubLetter == letter) return@ElasticAlphabetStrip
                            scrubLetter = letter
                            scrollJob?.cancel()
                            scrollJob = scope.launch {
                                listState.scrollToItem(target)
                            }
                        },
                        onScrubEnd = { scrubLetter = null },
                    )
                }
            }
        }
    }
}

private data class LetterRange(val letter: Char, val startIndex: Int, val endIndex: Int)

/** Continuous glide to [index]; teleports near the target for long jumps, then eases in. */
private suspend fun LazyListState.smoothScrollToIndex(index: Int) {
    val visible = layoutInfo.visibleItemsInfo
    if (visible.isEmpty()) {
        scrollToItem(index)
        return
    }

    val distance = abs(index - firstVisibleItemIndex)
    // Skip composing the whole A–Z span — land nearby, then ease the last stretch.
    if (distance > 10) {
        val approach = if (index > firstVisibleItemIndex) {
            (index - 6).coerceAtLeast(firstVisibleItemIndex)
        } else {
            (index + 6).coerceAtMost(firstVisibleItemIndex)
        }
        scrollToItem(approach)
    }

    val sizes = layoutInfo.visibleItemsInfo
    val avgSize = sizes.map { it.size }.average().toFloat().coerceAtLeast(1f)
    val estimatedPx =
        (index - firstVisibleItemIndex) * avgSize - firstVisibleItemScrollOffset
    if (abs(estimatedPx) < 1f) {
        scrollToItem(index)
        return
    }
    val durationMs = (280 + abs(estimatedPx) / 6f).roundToInt().coerceIn(280, 700)
    animateScrollBy(
        value = estimatedPx,
        animationSpec = tween(durationMillis = durationMs, easing = FastOutSlowInEasing),
    )
    if (firstVisibleItemIndex != index || firstVisibleItemScrollOffset != 0) {
        animateScrollToItem(index)
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
