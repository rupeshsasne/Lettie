package com.radix2.llm.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * A–Z fast scrubber. Uses equal vertical slots (Column weights) so letters never
 * pile up when height is short — elastic stretch is horizontal only.
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
            .width(40.dp)
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
        val slotPx = heightPx / letters.size.coerceAtLeast(1)
        val fontSp = with(density) {
            (slotPx * 0.72f).toSp().value.coerceIn(8f, 13f).sp
        }
        val allowElastic = slotPx >= with(density) { 14.dp.toPx() }

        val selectedIndex = activeLetter?.let { letters.indexOf(it) } ?: -1
        val touchIndex = if (!touchY.isNaN()) {
            ((touchY / heightPx) * letters.lastIndex).coerceIn(0f, letters.lastIndex.toFloat())
        } else {
            selectedIndex.toFloat()
        }

        if (dragging && activeLetter != null && allowElastic) {
            val bubbleY = if (!touchY.isNaN()) touchY else {
                (selectedIndex.coerceAtLeast(0).toFloat() / letters.lastIndex) * heightPx
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset {
                        IntOffset(
                            x = with(density) { (-40).dp.roundToPx() },
                            y = (bubbleY - with(density) { 20.dp.toPx() }).roundToInt(),
                        )
                    }
                    .size(40.dp)
                    .background(primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = activeLetter.toString(),
                    color = onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            letters.forEachIndexed { index, letter ->
                val distance = if (dragging || letter == activeLetter) {
                    abs(index - touchIndex)
                } else {
                    Float.MAX_VALUE
                }
                val influence = when {
                    !allowElastic -> 0f
                    dragging -> (1f - (distance / 5.5f)).coerceIn(0f, 1f)
                    letter == activeLetter && letter in available -> 0.35f
                    else -> 0f
                }
                val elastic = sin(influence * PI / 2).toFloat().let { it * it }
                val lastIndex = (letters.size - 1).coerceAtLeast(1)
                val normalized = (index.toFloat() / lastIndex - 0.5f) * 2f

                val targetStretch = if (allowElastic) -(elastic * 28f) else 0f
                val targetScale = when {
                    !allowElastic && letter == activeLetter -> 1.15f
                    dragging && distance < 0.55f -> 1.55f
                    dragging -> 1f + elastic * 0.45f
                    letter == activeLetter && letter in available -> 1.2f
                    else -> 1f
                }

                val stretch by animateFloatAsState(
                    targetValue = targetStretch,
                    animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium),
                    label = "azStretch$letter",
                )
                val scale by animateFloatAsState(
                    targetValue = targetScale,
                    animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium),
                    label = "azScale$letter",
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = letter.toString(),
                        fontSize = fontSp,
                        fontWeight = when {
                            letter == activeLetter -> FontWeight.Bold
                            elastic > 0.4f -> FontWeight.SemiBold
                            else -> FontWeight.Medium
                        },
                        color = when {
                            letter !in available -> muted
                            letter == activeLetter -> primary
                            else -> idle
                        },
                        modifier = Modifier.graphicsLayer {
                            translationX = stretch
                            scaleX = scale
                            scaleY = scale
                            // Tiny tilt only when there's room — never causes vertical pile-up.
                            rotationZ = if (allowElastic) normalized * 2f else 0f
                        },
                    )
                }
            }
        }
    }
}
