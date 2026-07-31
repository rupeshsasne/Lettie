package com.radix2.llm.ui.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.radix2.llm.ui.theme.LettieDimens

/** Centers content up to [maxWidth] so landscape / tablet layouts don't stretch endlessly. */
@Composable
fun MaxWidthContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = LettieDimens.contentMaxWidth,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(modifier = Modifier.widthIn(max = maxWidth).fillMaxWidth()) {
            content()
        }
    }
}

/**
 * Side-by-side panes when [dualPane] is true; otherwise stacks [start] above [end]
 * with flexible weights for landscape-friendly game layouts.
 */
@Composable
fun AdaptiveSplit(
    modifier: Modifier = Modifier,
    dualPane: Boolean = LocalWindowSize.current.useDualPane || LocalWindowSize.current.isLandscape,
    gap: Dp = LettieDimens.paneGap,
    startWeight: Float = 0.48f,
    endWeight: Float = 0.52f,
    start: @Composable () -> Unit,
    end: @Composable () -> Unit,
) {
    if (dualPane) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            Box(modifier = Modifier.weight(startWeight).fillMaxHeight()) { start() }
            Box(modifier = Modifier.weight(endWeight).fillMaxHeight()) { end() }
        }
    } else {
        androidx.compose.foundation.layout.Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(gap),
        ) {
            Box(modifier = Modifier.fillMaxWidth().weight(startWeight)) { start() }
            Box(modifier = Modifier.fillMaxWidth().weight(endWeight)) { end() }
        }
    }
}

/** List | detail on Medium+ when detail is showing; compact shows one surface at a time. */
@Composable
fun ListDetailLayout(
    showDetail: Boolean,
    modifier: Modifier = Modifier,
    list: @Composable () -> Unit,
    detail: @Composable () -> Unit,
) {
    val window = LocalWindowSize.current
    when {
        window.useDualPane && showDetail -> {
            Row(
                modifier = modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(LettieDimens.paneGap),
            ) {
                Box(modifier = Modifier.weight(0.42f).fillMaxHeight()) { list() }
                Box(modifier = Modifier.weight(0.58f).fillMaxHeight()) { detail() }
            }
        }
        window.useDualPane -> Box(modifier = modifier.fillMaxSize()) { list() }
        showDetail -> Box(modifier = modifier.fillMaxSize()) { detail() }
        else -> Box(modifier = modifier.fillMaxSize()) { list() }
    }
}

/** Adaptive height for hero cards (flashcards) from available constraints. */
@Composable
fun AdaptiveHeroCardHeight(modifier: Modifier = Modifier, content: @Composable (Dp) -> Unit) {
    val window = LocalWindowSize.current
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val fraction = when {
            window.heightSizeClass == LettieHeightSizeClass.Compact -> 0.62f
            window.isLandscape -> 0.78f
            else -> 0.52f
        }
        val height = (maxHeight * fraction).coerceIn(220.dp, 520.dp)
        Box(modifier = Modifier.fillMaxWidth().heightIn(min = height, max = height)) {
            content(height)
        }
    }
}
