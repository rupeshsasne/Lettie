package com.radix2.llm.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Soft teal atmosphere for brand-forward surfaces.
 * Layered washes — calm depth without noisy decoration.
 */
@Composable
fun LettieAtmosphere(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val dark = isSystemInDarkTheme()
    val top = if (dark) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
    } else {
        LettieBrand.TealFoam
    }
    val mid = if (dark) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
    }
    val bottom = MaterialTheme.colorScheme.surface
    val accent = if (dark) {
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.22f)
    } else {
        LettieBrand.Amber.copy(alpha = 0.30f)
    }
    val coralWash = if (dark) {
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f)
    } else {
        LettieBrand.Coral.copy(alpha = 0.18f)
    }

    BoxWithConstraints(modifier = modifier) {
        val w = constraints.maxWidth.toFloat().coerceAtLeast(1f)
        val h = constraints.maxHeight.toFloat().coerceAtLeast(1f)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to top,
                            0.48f to mid,
                            1f to bottom,
                        ),
                    ),
                )
                .background(
                    Brush.radialGradient(
                        colors = listOf(accent, Color.Transparent),
                        center = Offset(w * 0.18f, h * 0.12f),
                        radius = w * 0.85f,
                    ),
                )
                .background(
                    Brush.radialGradient(
                        colors = listOf(coralWash, Color.Transparent),
                        center = Offset(w * 0.92f, h * 0.58f),
                        radius = w * 0.7f,
                    ),
                ),
            content = content,
        )
    }
}

@Composable
fun letterStageBrush(): Brush {
    val dark = isSystemInDarkTheme()
    return Brush.verticalGradient(
        colors = if (dark) {
            listOf(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.surfaceContainerHigh,
            )
        } else {
            listOf(
                LettieBrand.TealMist,
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.surfaceContainerLowest,
            )
        },
    )
}

/** Soft floating letter crumbs — brand echo of the launcher trail. */
object LettieLetterTrail {
    val crumbs = listOf("A", "B", "C", "D", "E", "F", "G")
}
