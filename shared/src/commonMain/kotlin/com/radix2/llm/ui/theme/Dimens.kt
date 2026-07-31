package com.radix2.llm.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Spacing and layout tokens — generous for little fingers, calm visual rhythm. */
object LettieDimens {
    val spaceXs: Dp = 4.dp
    val spaceSm: Dp = 8.dp
    val spaceMd: Dp = 16.dp
    val spaceLg: Dp = 24.dp
    val spaceXl: Dp = 32.dp
    val spaceXxl: Dp = 40.dp

    /** Minimum touch target (M3 / accessibility). */
    val minTouch: Dp = 48.dp

    val screenPadding: Dp = 24.dp
    val cardPadding: Dp = 20.dp

    /** Cap content width on large landscape / tablet so lines don't stretch forever. */
    val contentMaxWidth: Dp = 840.dp
    val paneGap: Dp = 16.dp

    val navItemMinHeight: Dp = 56.dp
    val primaryCtaHeight: Dp = 68.dp
}
