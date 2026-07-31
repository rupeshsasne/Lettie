package com.radix2.llm.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Material 3 theme for Lettie — tonal teal + coral, Fredoka type, expressive shapes.
 * @see https://m3.material.io/
 */
@Composable
fun LastLetterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) LettieDarkScheme else LettieLightScheme,
        shapes = LettieShapes,
        typography = rememberLettieTypography(),
        content = content,
    )
}
