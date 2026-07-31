package com.radix2.llm.ui.adaptive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

/**
 * Media collapsing top bar — **one** title travels from the hero
 * (large, bottom of image) into the compact app-bar slot as you scroll.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingMediaTopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier,
    expandedHeight: Dp = 320.dp,
    collapsedHeight: Dp = 64.dp,
    actions: @Composable RowScope.() -> Unit = {},
    media: @Composable BoxScope.() -> Unit,
) {
    val density = LocalDensity.current
    val statusBars = WindowInsets.statusBars
    val statusBarDp = with(density) { statusBars.getTop(this).toDp() }
    val totalExpanded = expandedHeight + statusBarDp
    val totalCollapsed = collapsedHeight + statusBarDp

    SideEffect {
        scrollBehavior.state.heightOffsetLimit =
            with(density) { -(totalExpanded - totalCollapsed).toPx() }
    }

    val t = scrollBehavior.state.collapsedFraction.coerceIn(0f, 1f)
    // Smoothstep — title feels connected through the mid-scroll.
    val travel = t * t * (3f - 2f * t)
    val height = lerp(totalExpanded, totalCollapsed, t)
    val parallax = with(density) {
        (scrollBehavior.state.heightOffset * 0.35f).roundToInt()
    }
    val mediaAlpha = (1f - t * 0.88f).coerceIn(0.14f, 1f)

    val chromeColor = lerp(
        Color.White,
        MaterialTheme.colorScheme.onSurface,
        t,
    )
    val barFill = MaterialTheme.colorScheme.surface.copy(alpha = t)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        val maxH = constraints.maxHeight.toFloat().coerceAtLeast(1f)
        val maxW = constraints.maxWidth.toFloat().coerceAtLeast(1f)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = mediaAlpha
                    translationY = parallax.toFloat()
                },
            content = media,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color.Black.copy(alpha = 0.20f + 0.25f * t),
                            0.45f to Color.Transparent,
                            0.72f to Color.Black.copy(alpha = 0.35f * (1f - travel)),
                            1f to Color.Black.copy(alpha = 0.62f * (1f - travel)),
                        ),
                    ),
                ),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(totalCollapsed)
                .align(Alignment.TopCenter)
                .background(barFill),
        )

        // Icons only — title is a single traveling Text (not a second copy in the row).
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(collapsedHeight)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = chromeColor),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = "Back",
                    )
                }
            } else {
                Box(modifier = Modifier.width(12.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = actions,
            )
        }

        val startExpanded = with(density) { 24.dp.toPx() }
        val startCollapsed = with(density) {
            if (onBack != null) 56.dp.toPx() else 16.dp.toPx()
        }
        val endPad = with(density) { 16.dp.toPx() }
        val actionReserve = with(density) { 56.dp.toPx() }

        val fontSize = lerp(40.sp, 22.sp, travel)
        val blockHExpanded = with(density) { 52.dp.toPx() }
        val blockHCollapsed = with(density) { 28.dp.toPx() }

        val bottomPad = with(density) { 18.dp.toPx() }
        val expandedTop = maxH - bottomPad - blockHExpanded
        val collapsedTop = with(density) {
            statusBarDp.toPx() + (collapsedHeight.toPx() - blockHCollapsed) / 2f
        }

        val titleLeft = startExpanded + (startCollapsed - startExpanded) * travel
        val titleTop = expandedTop + (collapsedTop - expandedTop) * travel
        val titleWidthPx = (maxW - titleLeft - endPad - actionReserve * travel)
            .coerceAtLeast(with(density) { 120.dp.toPx() })
        val titleColor = lerp(Color.White, MaterialTheme.colorScheme.onSurface, travel)

        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                lineHeight = lerp(44.sp, 28.sp, travel),
            ),
            color = titleColor,
            maxLines = if (travel < 0.55f) 2 else 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .offset { IntOffset(titleLeft.roundToInt(), titleTop.roundToInt()) }
                .width(with(density) { titleWidthPx.toDp() }),
        )
    }
}
