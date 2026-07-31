package com.radix2.llm.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radix2.llm.ui.adaptive.AdaptiveSplit
import com.radix2.llm.ui.adaptive.LettieDestination
import com.radix2.llm.ui.adaptive.LettieHeightSizeClass
import com.radix2.llm.ui.adaptive.LocalWindowSize
import com.radix2.llm.ui.theme.LettieAtmosphere
import com.radix2.llm.ui.theme.LettieBrand
import com.radix2.llm.ui.theme.LettieDimens
import com.radix2.llm.ui.theme.LettieLetterTrail
import com.radix2.llm.ui.theme.LettieMotion
import kotlinx.coroutines.launch

/**
 * Play hub — one brand composition above the NavigationBar / beside the rail.
 */
@Composable
fun HomeScreen(
    onPlay: () -> Unit,
    onExplore: () -> Unit,
    onQuiz: () -> Unit,
    onFlashcards: () -> Unit,
    onProgress: () -> Unit,
    onVoice: () -> Unit,
    selectedDestination: LettieDestination = LettieDestination.Play,
    onDestinationSelected: (LettieDestination) -> Unit = {},
) {
    val window = LocalWindowSize.current
    val shortHeight = window.heightSizeClass == LettieHeightSizeClass.Compact || window.isLandscape

    MainScaffold(
        selected = selectedDestination,
        onDestinationSelected = { dest ->
            when (dest) {
                LettieDestination.Play -> Unit
                LettieDestination.Words -> onExplore()
                LettieDestination.Learn -> onFlashcards()
                LettieDestination.Me -> onProgress()
            }
            onDestinationSelected(dest)
        },
    ) { innerPadding ->
        LettieAtmosphere(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (window.isLandscape || window.useDualPane) {
                AdaptiveSplit(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(LettieDimens.screenPadding),
                    dualPane = true,
                    startWeight = 0.55f,
                    endWeight = 0.45f,
                    start = {
                        BrandStage(
                            modifier = Modifier.fillMaxSize(),
                            compact = true,
                            showTrail = false,
                        )
                    },
                    end = {
                        HomePrimaryActions(
                            onPlay = onPlay,
                            onVoice = onVoice,
                            onQuiz = onQuiz,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = LettieDimens.spaceMd),
                            compact = false,
                            showQuiz = true,
                        )
                    },
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = LettieDimens.screenPadding)
                        .padding(
                            top = LettieDimens.spaceMd,
                            bottom = LettieDimens.spaceSm,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BrandStage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        compact = shortHeight,
                        showTrail = !shortHeight,
                    )
                    Spacer(Modifier.height(LettieDimens.spaceMd))
                    HomePrimaryActions(
                        onPlay = onPlay,
                        onVoice = onVoice,
                        onQuiz = onQuiz,
                        modifier = Modifier.fillMaxWidth(),
                        compact = true,
                        showQuiz = !shortHeight,
                    )
                }
            }
        }
    }
}

@Composable
private fun BrandStage(
    modifier: Modifier = Modifier,
    compact: Boolean,
    showTrail: Boolean,
) {
    val brandScale = remember { Animatable(0.92f) }
    val brandAlpha = remember { Animatable(0f) }
    val markScale = remember { Animatable(0.7f) }

    LaunchedEffect(Unit) {
        launch { brandAlpha.animateTo(1f, LettieMotion.enterFade(480)) }
        launch { brandScale.animateTo(1f, LettieMotion.softSpring) }
        launch { markScale.animateTo(1f, LettieMotion.softSpring) }
    }

    Box(
        modifier = modifier
            .alpha(brandAlpha.value)
            .scale(brandScale.value),
        contentAlignment = Alignment.Center,
    ) {
        if (showTrail) {
            LetterTrailDecor()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LettieDimens.spaceSm),
        ) {
            Text(
                text = "Lettie",
                style = if (compact) {
                    MaterialTheme.typography.headlineLarge
                } else {
                    MaterialTheme.typography.displayMedium
                },
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Last Letter Master",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = LettieDimens.spaceXs),
            )
            // Tagline sits under brand — never below the fold in landscape.
            Text(
                text = "Hi! Let's play word chains.",
                style = if (compact) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.titleLarge
                },
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = LettieDimens.spaceSm),
            )

            Spacer(Modifier.height(if (compact) LettieDimens.spaceMd else LettieDimens.spaceLg))

            Surface(
                modifier = Modifier
                    .size(if (compact) 88.dp else 132.dp)
                    .scale(markScale.value),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.92f),
                shadowElevation = 4.dp,
                tonalElevation = 2.dp,
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "L",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = if (compact) 52.sp else 80.sp,
                        ),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-10).dp, y = 12.dp)
                            .size(12.dp),
                        shape = CircleShape,
                        color = LettieBrand.Amber,
                    ) {}
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = 12.dp, y = (-12).dp)
                            .size(9.dp),
                        shape = CircleShape,
                        color = LettieBrand.Coral,
                    ) {}
                }
            }
        }
    }
}

@Composable
private fun LetterTrailDecor() {
    val crumbs = LettieLetterTrail.crumbs
    val colors = listOf(
        Color.White.copy(alpha = 0.45f),
        LettieBrand.Coral.copy(alpha = 0.45f),
        LettieBrand.Amber.copy(alpha = 0.55f),
        Color.White.copy(alpha = 0.35f),
        LettieBrand.Coral.copy(alpha = 0.35f),
        LettieBrand.Amber.copy(alpha = 0.4f),
        LettieBrand.Coral.copy(alpha = 0.55f),
    )
    val offsets = listOf(
        -150 to -50, -90 to -90, -20 to -110, 50 to -105,
        110 to -75, 150 to -35, 140 to 16,
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        crumbs.forEachIndexed { i, letter ->
            val (dx, dy) = offsets[i]
            Text(
                text = letter,
                style = MaterialTheme.typography.titleMedium,
                color = colors[i],
                modifier = Modifier.offset(dx.dp, dy.dp),
            )
        }
    }
}

@Composable
private fun HomePrimaryActions(
    onPlay: () -> Unit,
    onVoice: () -> Unit,
    onQuiz: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean,
    showQuiz: Boolean,
) {
    val ctaAlpha = remember { Animatable(0f) }
    val ctaOffset = remember { Animatable(24f) }
    LaunchedEffect(Unit) {
        launch { ctaAlpha.animateTo(1f, tween(420, delayMillis = 120, easing = FastOutSlowInEasing)) }
        launch { ctaOffset.animateTo(0f, LettieMotion.softSpring) }
    }

    Column(
        modifier = modifier
            .alpha(ctaAlpha.value)
            .offset(y = ctaOffset.value.dp),
        verticalArrangement = if (compact) {
            Arrangement.spacedBy(LettieDimens.spaceSm)
        } else {
            Arrangement.spacedBy(LettieDimens.spaceMd, Alignment.CenterVertically)
        },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onPlay,
            modifier = Modifier
                .fillMaxWidth()
                .height(LettieDimens.primaryCtaHeight),
            shape = MaterialTheme.shapes.large,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        ) {
            Text("Play with Lettie", style = MaterialTheme.typography.titleLarge)
        }
        if (showQuiz) {
            FilledTonalButton(
                onClick = onQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LettieDimens.minTouch),
                shape = MaterialTheme.shapes.large,
            ) {
                Text("Quick quiz", style = MaterialTheme.typography.titleMedium)
            }
        }
        TextButton(
            onClick = onVoice,
            modifier = Modifier
                .fillMaxWidth()
                .height(LettieDimens.minTouch),
        ) {
            Text("Lettie's voice", style = MaterialTheme.typography.titleMedium)
        }
    }
}
