package com.radix2.llm.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Niagara Launcher–style elastic A–Z ribbon.
 *
 * Letters rest on a soft arc; under the finger they stretch left with a rubber-band
 * falloff and spring back on release.
 */
@Composable
fun ElasticAlphabetStrip(
    available: Set<Char>,
    activeLetter: Char?,
    onJumpLetter: (Char) -> Unit,
    onScrubLetter: (Char) -> Unit,
    onScrubEnd: () -> Unit,
    modifier: Modifier = Modifier,
    letters: List<Char> = ('A'..'Z').toList(),
) {
    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val idle = MaterialTheme.colorScheme.onSurfaceVariant
    val muted = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.32f)

    var dragging by remember { mutableStateOf(false) }
    var touchY by remember { mutableFloatStateOf(Float.NaN) }
    var lastScrubbed by remember { mutableStateOf<Char?>(null) }

    val onJumpLatest by rememberUpdatedState(onJumpLetter)
    val onScrubLatest by rememberUpdatedState(onScrubLetter)
    val onScrubEndLatest by rememberUpdatedState(onScrubEnd)

    fun pickLetter(y: Float, height: Float): Char? {
        if (height <= 0f || letters.isEmpty()) return null
        val progress = (y / height).coerceIn(0f, 1f)
        val idx = (progress * letters.lastIndex).roundToInt().coerceIn(0, letters.lastIndex)
        val letter = letters[idx]
        return letter.takeIf { it in available }
            ?: available.filter { it <= letter }.maxOrNull()
            ?: available.minOrNull()
    }

    fun scrubAt(y: Float, height: Float) {
        touchY = y
        val letter = pickLetter(y, height) ?: return
        if (letter != lastScrubbed) {
            lastScrubbed = letter
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onScrubLatest(letter)
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .width(56.dp)
            .fillMaxHeight()
            .pointerInput(available, letters) {
                detectTapGestures { offset ->
                    val letter = pickLetter(offset.y, size.height.toFloat()) ?: return@detectTapGestures
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onJumpLatest(letter)
                }
            }
            .pointerInput(available, letters) {
                detectVerticalDragGestures(
                    onDragStart = { offset ->
                        dragging = true
                        lastScrubbed = null
                        scrubAt(offset.y, size.height.toFloat())
                    },
                    onDragEnd = {
                        dragging = false
                        touchY = Float.NaN
                        lastScrubbed = null
                        onScrubEndLatest()
                    },
                    onDragCancel = {
                        dragging = false
                        touchY = Float.NaN
                        lastScrubbed = null
                        onScrubEndLatest()
                    },
                    onVerticalDrag = { change, _ ->
                        change.consume()
                        scrubAt(change.position.y, size.height.toFloat())
                    },
                )
            },
    ) {
        val heightPx = constraints.maxHeight.toFloat().coerceAtLeast(1f)
        val selectedIndex = activeLetter?.let { letters.indexOf(it) } ?: -1
        val touchIndex = if (!touchY.isNaN()) {
            ((touchY / heightPx) * letters.lastIndex).coerceIn(0f, letters.lastIndex.toFloat())
        } else {
            selectedIndex.toFloat()
        }

        if (dragging && activeLetter != null) {
            val bubbleLetter = activeLetter
            val bubbleY = if (!touchY.isNaN()) {
                touchY
            } else {
                (selectedIndex.coerceAtLeast(0).toFloat() / letters.lastIndex) * heightPx
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset {
                        IntOffset(
                            x = with(density) { (-44).dp.roundToPx() },
                            y = (bubbleY - with(density) { 22.dp.toPx() }).roundToInt(),
                        )
                    }
                    .size(44.dp)
                    .background(primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = bubbleLetter.toString(),
                    color = onPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        letters.forEachIndexed { index, letter ->
            key(letter) {
                ElasticLetterGlyph(
                    letter = letter,
                    index = index,
                    letterCount = letters.size,
                    heightPx = heightPx,
                    touchIndex = touchIndex,
                    dragging = dragging,
                    enabled = letter in available,
                    selected = letter == activeLetter,
                    primary = primary,
                    idle = idle,
                    muted = muted,
                    modifier = Modifier.align(Alignment.TopEnd),
                )
            }
        }
    }
}

@Composable
private fun ElasticLetterGlyph(
    letter: Char,
    index: Int,
    letterCount: Int,
    heightPx: Float,
    touchIndex: Float,
    dragging: Boolean,
    enabled: Boolean,
    selected: Boolean,
    primary: Color,
    idle: Color,
    muted: Color,
    modifier: Modifier = Modifier,
) {
    val lastIndex = (letterCount - 1).coerceAtLeast(1)
    val progress = if (letterCount == 1) 0.5f else index.toFloat() / lastIndex
    val baseY = progress * heightPx

    val normalized = (progress - 0.5f) * 2f
    val restingCurve = (1f - abs(normalized)) * 6f

    val distance = if (dragging || selected) abs(index - touchIndex) else Float.MAX_VALUE
    val influence = when {
        dragging -> (1f - (distance / 5.5f)).coerceIn(0f, 1f)
        selected && enabled -> 0.35f
        else -> 0f
    }
    val elastic = sin(influence * PI / 2).toFloat().let { it * it }

    val targetStretch = -(restingCurve + elastic * 78f)
    val targetScale = when {
        dragging && distance < 0.55f -> 2.15f
        dragging -> 1f + elastic * 1.05f
        selected && enabled -> 1.35f
        else -> 1f
    }
    val targetRotation = if (dragging && elastic > 0f) {
        normalized * 6f + elastic * 12f * -normalized.signOrZero()
    } else {
        normalized * 3f
    }
    val targetAlpha = when {
        !enabled -> 0.28f
        dragging && elastic > 0.05f -> 0.55f + elastic * 0.45f
        selected -> 1f
        else -> 0.72f
    }

    val stretch by animateFloatAsState(
        targetValue = targetStretch,
        animationSpec = spring(dampingRatio = 0.48f, stiffness = Spring.StiffnessLow),
        label = "azStretch$letter",
    )
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(dampingRatio = 0.42f, stiffness = Spring.StiffnessMediumLow),
        label = "azScale$letter",
    )
    val rotation by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow),
        label = "azRot$letter",
    )
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "azAlpha$letter",
    )

    Text(
        text = letter.toString(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = when {
            selected -> FontWeight.Bold
            elastic > 0.4f -> FontWeight.SemiBold
            else -> FontWeight.Medium
        },
        color = when {
            !enabled -> muted
            selected -> primary
            else -> idle
        },
        modifier = modifier.graphicsLayer {
            translationX = stretch
            translationY = baseY - size.height / 2f
            scaleX = scale
            scaleY = scale
            rotationZ = rotation
            this.alpha = alpha
        },
    )
}

private fun Float.signOrZero(): Float = when {
    this > 0f -> 1f
    this < 0f -> -1f
    else -> 0f
}
