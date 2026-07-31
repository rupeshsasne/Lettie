package com.radix2.llm.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radix2.llm.data.LetterCoverage
import com.radix2.llm.data.ProgressStore
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Pronunciation
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.Word
import com.radix2.llm.ui.adaptive.AdaptiveSplit
import com.radix2.llm.ui.adaptive.LettieDestination
import com.radix2.llm.ui.adaptive.LocalWindowSize
import com.radix2.llm.ui.theme.LettieAtmosphere
import com.radix2.llm.ui.theme.LettieDimens
import com.radix2.llm.ui.theme.letterStageBrush
import com.radix2.llm.voice.VoiceController
import kotlin.random.Random
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.ic_volume_up
import org.jetbrains.compose.resources.painterResource

/**
 * Immersive study surface — one card owns the viewport; filters are quiet chrome.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardsScreen(
    repo: WordRepository,
    voice: VoiceController,
    progressStore: ProgressStore,
    onExit: () -> Unit,
    focusLetter: Char? = null,
    focusCategoryNames: List<String> = emptyList(),
    selectedDestination: LettieDestination? = LettieDestination.Learn,
    onDestinationSelected: ((LettieDestination) -> Unit)? = null,
) {
    val practiceLetter = focusLetter?.uppercaseChar()?.takeIf { it.isLetter() }
    var selectedRoundNames by remember {
        mutableStateOf(
            if (focusCategoryNames.isNotEmpty()) {
                Round.entries
                    .filter { round ->
                        round.categories.any { cat -> cat.name in focusCategoryNames }
                    }
                    .map { it.name }
            } else {
                emptyList()
            },
        )
    }
    val categories = remember(selectedRoundNames, focusCategoryNames) {
        when {
            selectedRoundNames.isNotEmpty() -> selectedRoundNames.mapNotNull { name ->
                Round.entries.find { it.name == name }
            }.flatMap { it.categories }.distinct()
            focusCategoryNames.isNotEmpty() -> focusCategoryNames.mapNotNull { name ->
                Category.entries.find { it.name == name }
            }
            else -> Category.entries.toList()
        }
    }
    // Fresh shuffle each visit / filter change so we don't always open on Elephant.
    val sessionSeed = remember { Random.nextInt() }
    val deck = remember(selectedRoundNames, practiceLetter, categories, sessionSeed) {
        varietyDeck(
            repo = repo,
            categories = categories,
            exposure = progressStore::exposure,
            random = Random(sessionSeed),
            focusLetter = practiceLetter,
        )
    }

    var index by remember(selectedRoundNames, practiceLetter, sessionSeed) { mutableStateOf(0) }
    var flipped by remember(selectedRoundNames, practiceLetter, sessionSeed) { mutableStateOf(false) }

    val card = deck.getOrNull(index)
    val studiedThisVisit = remember(sessionSeed) { mutableSetOf<String>() }
    LaunchedEffect(card?.id) {
        val id = card?.id ?: return@LaunchedEffect
        if (studiedThisVisit.add(id)) progressStore.noteStudied(id)
    }
    LaunchedEffect(practiceLetter) {
        if (practiceLetter != null) {
            voice.speak("Let's practice words that start with $practiceLetter!")
        }
    }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(420),
        label = "flip",
    )
    val progress = if (deck.isEmpty()) 0f else (index + 1f) / deck.size

    val window = LocalWindowSize.current
    val scaffold: @Composable (@Composable (PaddingValues) -> Unit) -> Unit = { content ->
        if (selectedDestination != null && onDestinationSelected != null) {
            MainScaffold(
                selected = selectedDestination,
                onDestinationSelected = onDestinationSelected,
                title = null,
                content = content,
            )
        } else {
            AppScaffold(title = "Flashcards", onBack = onExit, content = content)
        }
    }

    scaffold { innerPadding ->
        LettieAtmosphere(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LettieDimens.screenPadding)
                    .padding(top = LettieDimens.spaceSm, bottom = LettieDimens.spaceMd),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            if (practiceLetter != null) "Practice $practiceLetter" else "Learn",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            when {
                                deck.isEmpty() -> "No cards yet"
                                practiceLetter != null ->
                                    "${index + 1} of ${deck.size} · words starting with $practiceLetter"
                                else -> "${index + 1} of ${deck.size}"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    TextButton(onClick = { voice.speak(card?.name ?: "Let's learn!") }) {
                        Text("Hear")
                    }
                }

                Spacer(Modifier.height(LettieDimens.spaceSm))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(MaterialTheme.shapes.extraSmall),
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                )

                Spacer(Modifier.height(LettieDimens.spaceSm))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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

                Spacer(Modifier.height(LettieDimens.spaceMd))

                if (card == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("No cards here yet.", style = MaterialTheme.typography.titleMedium)
                    }
                    return@Column
                }

                val showingBack = rotation > 90f
                if (window.isLandscape || window.useDualPane) {
                    AdaptiveSplit(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        dualPane = true,
                        start = {
                            FlashcardStage(
                                card = card,
                                rotation = rotation,
                                showingBack = showingBack,
                                onFlip = { flipped = !flipped },
                                voice = voice,
                                modifier = Modifier.fillMaxSize().padding(end = 8.dp),
                            )
                        },
                        end = {
                            FlashcardControls(
                                canPrev = index > 0,
                                canNext = index < deck.size - 1,
                                onPrev = { index--; flipped = false },
                                onNext = { index++; flipped = false },
                                onFlip = { flipped = !flipped },
                                flipped = flipped,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(start = 8.dp),
                            )
                        },
                    )
                } else {
                    FlashcardStage(
                        card = card,
                        rotation = rotation,
                        showingBack = showingBack,
                        onFlip = { flipped = !flipped },
                        voice = voice,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                    Spacer(Modifier.height(LettieDimens.spaceMd))
                    FlashcardControls(
                        canPrev = index > 0,
                        canNext = index < deck.size - 1,
                        onPrev = { index--; flipped = false },
                        onNext = { index++; flipped = false },
                        onFlip = { flipped = !flipped },
                        flipped = flipped,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun FlashcardControls(
    canPrev: Boolean,
    canNext: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onFlip: () -> Unit,
    flipped: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LettieDimens.spaceSm, Alignment.CenterVertically),
    ) {
        Button(
            onClick = onFlip,
            modifier = Modifier
                .fillMaxWidth()
                .height(LettieDimens.primaryCtaHeight),
            shape = MaterialTheme.shapes.large,
        ) {
            Text(
                if (flipped) "Show picture" else "Reveal word",
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FilledTonalButton(
                onClick = onPrev,
                enabled = canPrev,
                modifier = Modifier
                    .weight(1f)
                    .height(LettieDimens.navItemMinHeight),
                shape = MaterialTheme.shapes.large,
            ) { Text("Back") }
            FilledTonalButton(
                onClick = onNext,
                enabled = canNext,
                modifier = Modifier
                    .weight(1f)
                    .height(LettieDimens.navItemMinHeight),
                shape = MaterialTheme.shapes.large,
            ) { Text("Next") }
        }
    }
}

@Composable
private fun FlashcardStage(
    card: Word,
    rotation: Float,
    showingBack: Boolean,
    onFlip: () -> Unit,
    voice: VoiceController,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 14f * density
            }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onFlip,
            ),
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 6.dp,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        if (!showingBack) {
            // Image is the card — edge to edge, hint overlaid (no nested boxes).
            Box(modifier = Modifier.fillMaxSize()) {
                WordImage(
                    word = card,
                    fill = true,
                    emojiFallbackSize = 120.sp,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.55f),
                                ),
                            ),
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Tap to reveal",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(letterStageBrush())
                    .graphicsLayer { rotationY = 180f },
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = LettieDimens.spaceXl),
                ) {
                    Text(
                        card.name,
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(LettieDimens.spaceSm))
                    Text(
                        Pronunciation.sayItLike(card),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(LettieDimens.spaceXs))
                    Text(
                        card.category.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(LettieDimens.spaceLg))
                    FilledTonalButton(
                        onClick = { voice.speak(card.name) },
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.height(LettieDimens.navItemMinHeight),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_volume_up),
                            contentDescription = null,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Hear it", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

/**
 * Study deck that still favors “jammed” ending letters for practice, but:
 * - shuffles letter order each session (not always E → Elephant)
 * - prefers words the child has seen / said less often
 */
private fun varietyDeck(
    repo: WordRepository,
    categories: List<Category>,
    exposure: (String) -> Int,
    random: Random,
    focusLetter: Char? = null,
): List<Word> {
    var words = repo.all.filter { it.category in categories }
    if (focusLetter != null) {
        val focused = words.filter { it.firstLetter.equals(focusLetter, ignoreCase = true) }
        // If the game categories are thin for that letter, widen to the whole bank.
        words = focused.ifEmpty {
            repo.all.filter { it.firstLetter.equals(focusLetter, ignoreCase = true) }
        }
        return words.shuffled(random).sortedBy { exposure(it.id) }
    }
    if (words.isEmpty()) return emptyList()

    val rounds = Round.entries.filter { round -> round.categories.any { it in categories } }
        .ifEmpty { Round.entries.toList() }
    val priorityLetters = rounds.flatMap { LetterCoverage.topEndings(it) }.distinct().shuffled(random)
    val otherLetters = words.map { it.firstLetter.uppercaseChar() }
        .distinct()
        .filter { it !in priorityLetters }
        .shuffled(random)

    return (priorityLetters + otherLetters).flatMap { letter ->
        words.filter { it.firstLetter.equals(letter, ignoreCase = true) }
            .shuffled(random)
            .sortedBy { exposure(it.id) }
    }
}
