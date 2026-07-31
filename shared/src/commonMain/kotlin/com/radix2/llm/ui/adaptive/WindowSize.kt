package com.radix2.llm.ui.adaptive

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** M3-style width breakpoints (Compose Multiplatform–friendly). */
enum class LettieWidthSizeClass { Compact, Medium, Expanded }

enum class LettieHeightSizeClass { Compact, Medium, Expanded }

@Immutable
data class LettieWindowSize(
    val widthSizeClass: LettieWidthSizeClass,
    val heightSizeClass: LettieHeightSizeClass,
    val width: Dp,
    val height: Dp,
) {
    /** Side rail in landscape phones and tablets — not a second content pane. */
    val useNavigationRail: Boolean
        get() = isLandscape || widthSizeClass != LettieWidthSizeClass.Compact

    /**
     * List|detail only when there is real horizontal *and* vertical room.
     * Phone landscape is wide-but-short → single pane (avoids crushed A–Z / wrapped titles).
     */
    val useDualPane: Boolean
        get() = when {
            widthSizeClass == LettieWidthSizeClass.Expanded &&
                heightSizeClass != LettieHeightSizeClass.Compact -> true
            widthSizeClass == LettieWidthSizeClass.Medium &&
                heightSizeClass == LettieHeightSizeClass.Expanded -> true
            else -> false
        }

    val isLandscape: Boolean get() = width > height
}

val LocalWindowSize = staticCompositionLocalOf {
    LettieWindowSize(
        widthSizeClass = LettieWidthSizeClass.Compact,
        heightSizeClass = LettieHeightSizeClass.Medium,
        width = 400.dp,
        height = 800.dp,
    )
}

fun calculateWindowSize(maxWidth: Dp, maxHeight: Dp): LettieWindowSize {
    val widthClass = when {
        maxWidth < 600.dp -> LettieWidthSizeClass.Compact
        maxWidth < 840.dp -> LettieWidthSizeClass.Medium
        else -> LettieWidthSizeClass.Expanded
    }
    val heightClass = when {
        maxHeight < 480.dp -> LettieHeightSizeClass.Compact
        maxHeight < 900.dp -> LettieHeightSizeClass.Medium
        else -> LettieHeightSizeClass.Expanded
    }
    return LettieWindowSize(widthClass, heightClass, maxWidth, maxHeight)
}

@Composable
fun ProvideWindowSize(content: @Composable () -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val size = remember(maxWidth, maxHeight) { calculateWindowSize(maxWidth, maxHeight) }
        CompositionLocalProvider(LocalWindowSize provides size, content = content)
    }
}
