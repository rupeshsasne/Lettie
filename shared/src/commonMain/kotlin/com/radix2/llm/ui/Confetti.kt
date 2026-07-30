package com.radix2.llm.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.sin
import kotlin.random.Random

private data class Confetto(
    val xFraction: Float,
    val color: Color,
    val size: Float,
    val speed: Float,
    val sway: Float,
    val phase: Float,
    val spin: Float,
)

private val ConfettiColors = listOf(
    Color(0xFFFF6B6B),
    Color(0xFFFFD93D),
    Color(0xFF6BCB77),
    Color(0xFF4D96FF),
    Color(0xFFB983FF),
    Color(0xFFFF9F45),
)

/**
 * A lightweight, dependency-free confetti burst. Increment [trigger] to fire a new burst;
 * each burst falls and fades over ~1.6s. Rendered on a Canvas so it needs no Lottie/assets.
 */
@Composable
fun ConfettiOverlay(trigger: Int, modifier: Modifier = Modifier) {
    val progress = remember(trigger) { Animatable(0f) }
    val pieces = remember(trigger) {
        val rnd = Random(trigger * 9973 + 1)
        List(70) {
            Confetto(
                xFraction = rnd.nextFloat(),
                color = ConfettiColors[rnd.nextInt(ConfettiColors.size)],
                size = 10f + rnd.nextFloat() * 14f,
                speed = 0.8f + rnd.nextFloat() * 0.6f,
                sway = 4f + rnd.nextFloat() * 6f,
                phase = rnd.nextFloat() * 6.28f,
                spin = -180f + rnd.nextFloat() * 360f,
            )
        }
    }

    LaunchedEffect(trigger) {
        if (trigger > 0) {
            progress.snapTo(0f)
            progress.animateTo(1f, animationSpec = tween(1600, easing = LinearEasing))
        }
    }

    val p = progress.value
    if (trigger > 0 && p < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val alpha = if (p < 0.8f) 1f else (1f - (p - 0.8f) / 0.2f)
            pieces.forEach { c ->
                val x = c.xFraction * size.width + sin(p * c.sway + c.phase) * 24f
                val y = (-0.15f + p * 1.25f * c.speed) * size.height
                if (y in -30f..(size.height + 30f)) {
                    rotate(degrees = p * c.spin, pivot = Offset(x, y)) {
                        drawRect(
                            color = c.color.copy(alpha = alpha),
                            topLeft = Offset(x - c.size / 2f, y - c.size / 2f),
                            size = Size(c.size, c.size * 0.6f),
                        )
                    }
                }
            }
        }
    }
}
