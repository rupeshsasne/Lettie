package com.radix2.llm.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

/** Shared motion language — soft springs for delight, short fades for chrome. */
object LettieMotion {
    val softSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )

    val snappySpring = spring<Float>(
        dampingRatio = 0.75f,
        stiffness = Spring.StiffnessMedium,
    )

    fun <T> enterFade(durationMs: Int = 420) = tween<T>(durationMs)

    fun <T> settle(durationMs: Int = 280) = tween<T>(durationMs)
}
