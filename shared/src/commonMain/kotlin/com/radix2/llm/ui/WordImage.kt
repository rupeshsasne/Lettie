package com.radix2.llm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.radix2.llm.data.WordImages
import com.radix2.llm.domain.Word

/**
 * Loads a real photograph from a URL at runtime (Wikipedia thumbnail via [WordImages],
 * or [Word.imageUrl] when set). Falls back to the word's emoji while resolving / offline.
 * No images are bundled in the APK.
 */
@Composable
fun WordImage(
    word: Word,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    emojiFallbackSize: TextUnit = 56.sp,
    contentScale: ContentScale = ContentScale.Crop,
    /** When true, fills [modifier] bounds (no fixed [size]) — for collapsing headers. */
    fill: Boolean = false,
    shape: Shape? = null,
) {
    var imageUrl by remember(word.id) { mutableStateOf(word.imageUrl) }
    var resolving by remember(word.id) { mutableStateOf(word.imageUrl == null) }
    var failed by remember(word.id) { mutableStateOf(false) }

    LaunchedEffect(word.id) {
        failed = false
        if (word.imageUrl == null) {
            resolving = true
            imageUrl = WordImages.urlFor(word)
            resolving = false
            if (imageUrl == null) failed = true
        } else {
            imageUrl = word.imageUrl
            resolving = false
        }
    }

    val clipShape = shape ?: if (fill) RectangleShape else MaterialTheme.shapes.large
    val boxModifier = if (fill) {
        modifier
            .fillMaxSize()
            .clip(clipShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
    } else {
        modifier
            .size(size)
            .clip(clipShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
    }

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center,
    ) {
        // Always keep emoji underneath so we have a fallback if the URL fails.
        Text(word.emoji, fontSize = emojiFallbackSize)

        val url = imageUrl
        if (url != null && !failed) {
            AsyncImage(
                model = url,
                contentDescription = word.name,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize(),
                onError = { failed = true },
            )
        }

        if (resolving) {
            CircularProgressIndicator(
                modifier = Modifier.size(if (fill) 36.dp else size / 4),
                strokeWidth = 2.dp,
            )
        }
    }
}
