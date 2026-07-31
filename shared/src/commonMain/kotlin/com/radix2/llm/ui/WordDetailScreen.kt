package com.radix2.llm.ui

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radix2.llm.data.FavoritesStore
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.KidFacts
import com.radix2.llm.domain.Pronunciation
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.Word
import com.radix2.llm.ui.adaptive.CollapsingMediaTopBar
import com.radix2.llm.ui.theme.LettieDimens
import com.radix2.llm.voice.VoiceController
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.ic_favorite
import lastlettermaster.shared.generated.resources.ic_favorite_border
import lastlettermaster.shared.generated.resources.ic_volume_up
import org.jetbrains.compose.resources.painterResource

/**
 * Word detail — media *is* the collapsing toolbar.
 * Title morphs from large-on-image → compact bar title; no duplicate image card.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailScreen(
    word: Word,
    repo: WordRepository,
    favorites: FavoritesStore,
    voice: VoiceController,
    onOpenWord: (Word) -> Unit,
    onBack: (() -> Unit)? = null,
    showScaffold: Boolean = true,
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

    if (!showScaffold) {
        EmbeddedDetail(
            word = word,
            isFav = isFav,
            round = round,
            sayItLike = sayItLike,
            facts = facts,
            nextWords = nextWords,
            voice = voice,
            onToggleFavorite = { favorites.toggle(word.id) },
            onOpenWord = onOpenWord,
        )
        return
    }

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)
    val collapsedFraction = scrollBehavior.state.collapsedFraction.coerceIn(0f, 1f)
    val actionTint = androidx.compose.ui.graphics.lerp(
        Color.White,
        MaterialTheme.colorScheme.onSurface,
        collapsedFraction,
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsingMediaTopBar(
                title = word.name,
                scrollBehavior = scrollBehavior,
                onBack = onBack,
                actions = {
                    IconButton(
                        onClick = { favorites.toggle(word.id) },
                        colors = IconButtonDefaults.iconButtonColors(contentColor = actionTint),
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isFav) Res.drawable.ic_favorite else Res.drawable.ic_favorite_border,
                            ),
                            contentDescription = if (isFav) "Remove favorite" else "Add favorite",
                            tint = if (isFav) {
                                MaterialTheme.colorScheme.error
                            } else {
                                actionTint
                            },
                        )
                    }
                },
                media = {
                    WordImage(
                        word = word,
                        fill = true,
                        emojiFallbackSize = 120.sp,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = LettieDimens.screenPadding,
                end = LettieDimens.screenPadding,
                top = innerPadding.calculateTopPadding() + LettieDimens.spaceMd,
                bottom = innerPadding.calculateBottomPadding() + LettieDimens.spaceXl,
            ),
            verticalArrangement = Arrangement.spacedBy(LettieDimens.spaceMd),
        ) {
            item {
                FilledTonalButton(
                    onClick = { voice.speak(word.name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LettieDimens.navItemMinHeight),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_volume_up),
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Hear the word", style = MaterialTheme.typography.titleMedium)
                }
            }
            item {
                Text(
                    word.category.displayName,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    "Say it like: $sayItLike",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            item {
                DetailFactsBlock(name = word.name, facts = facts)
            }
            item {
                DetailMetaBlock(word = word, round = round)
            }
            if (nextWords.isNotEmpty()) {
                item {
                    DetailNextBlock(
                        word = word,
                        nextWords = nextWords,
                        onOpenWord = onOpenWord,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmbeddedDetail(
    word: Word,
    isFav: Boolean,
    round: Round?,
    sayItLike: String,
    facts: List<String>,
    nextWords: List<Word>,
    voice: VoiceController,
    onToggleFavorite: () -> Unit,
    onOpenWord: (Word) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = LettieDimens.spaceLg),
        verticalArrangement = Arrangement.spacedBy(LettieDimens.spaceMd),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            ) {
                WordImage(
                    word = word,
                    fill = true,
                    emojiFallbackSize = 96.sp,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                Surface(
                    color = Color.Black.copy(alpha = 0.35f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            word.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            modifier = Modifier.weight(1f),
                        )
                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                painter = painterResource(
                                    if (isFav) Res.drawable.ic_favorite else Res.drawable.ic_favorite_border,
                                ),
                                contentDescription = null,
                                tint = Color.White,
                            )
                        }
                    }
                }
            }
        }
        item {
            Column(Modifier.padding(horizontal = LettieDimens.screenPadding)) {
                FilledTonalButton(
                    onClick = { voice.speak(word.name) },
                    modifier = Modifier.fillMaxWidth().height(LettieDimens.minTouch),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Icon(painter = painterResource(Res.drawable.ic_volume_up), contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Hear the word")
                }
                Spacer(Modifier.height(LettieDimens.spaceMd))
                DetailFactsBlock(word.name, facts)
                Spacer(Modifier.height(LettieDimens.spaceMd))
                DetailMetaBlock(word, round)
                if (nextWords.isNotEmpty()) {
                    Spacer(Modifier.height(LettieDimens.spaceMd))
                    DetailNextBlock(word, nextWords, onOpenWord)
                }
            }
        }
    }
}

@Composable
private fun DetailMetaBlock(
    word: Word,
    round: Round?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Word facts",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column {
                InfoItem("Category", word.category.displayName)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                round?.let {
                    InfoItem("Round", it.displayName)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                }
                InfoItem("Level", word.difficulty.label)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                InfoItem("Starts with", word.firstLetter.toString())
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                InfoItem("Ends with", word.lastLetter.toString())
                if (word.aliases.isNotEmpty()) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    InfoItem("Also called", word.aliases.joinToString(", "))
                }
                word.geo?.capital
                    ?.takeIf { it.isNotBlank() && it != "Capital" && it != "—" }
                    ?.let {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        InfoItem("Capital", it)
                    }
                word.geo?.countryOf
                    ?.takeIf { it.isNotBlank() && it != "World" }
                    ?.let {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        InfoItem("Country", it)
                    }
            }
        }
    }
}

@Composable
private fun DetailFactsBlock(
    name: String,
    facts: List<String>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.tertiaryContainer,
    ) {
        Column(Modifier.padding(LettieDimens.cardPadding)) {
            Text(
                "About $name",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            Text(
                text = facts[0],
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(top = 10.dp),
            )
            Text(
                text = facts[1],
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun DetailNextBlock(
    word: Word,
    nextWords: List<Word>,
    onOpenWord: (Word) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Play next · starts with ${word.lastLetter}",
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

@Composable
private fun InfoItem(label: String, value: String) {
    ListItem(
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = {
            Text(label, style = MaterialTheme.typography.bodyLarge)
        },
        trailingContent = {
            Text(value, style = MaterialTheme.typography.titleMedium)
        },
    )
}
