package com.radix2.llm.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Lettie design language — tonal palette seeded from the launcher mark:
 * vivid teal atmosphere, coral/amber accents for celebration & emphasis.
 * Full M3 surface-container ladder for soft elevation without heavy shadows.
 */

// Brand reference tokens (also used for atmospheric brushes outside ColorScheme).
internal object LettieBrand {
    val TealDeep = Color(0xFF00584F)
    val Teal = Color(0xFF00897A)
    val TealMist = Color(0xFF7EE8D8)
    val TealFoam = Color(0xFFC8FFF4)
    val Coral = Color(0xFFFF8A4C)
    val Amber = Color(0xFFFFC857)
    val Ink = Color(0xFF10211E)
}

internal val LettieLightScheme = lightColorScheme(
    primary = Color(0xFF006B5F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF7EE8D8),
    onPrimaryContainer = Color(0xFF00201C),
    secondary = Color(0xFF3F5F59),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFC5EDE5),
    onSecondaryContainer = Color(0xFF00201C),
    tertiary = Color(0xFFB34D1F),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDBCA),
    onTertiaryContainer = Color(0xFF390C00),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF2FBF8),
    onBackground = Color(0xFF141D1B),
    surface = Color(0xFFF2FBF8),
    onSurface = Color(0xFF141D1B),
    surfaceVariant = Color(0xFFD5E6E1),
    onSurfaceVariant = Color(0xFF3A4A46),
    outline = Color(0xFF6A7A75),
    outlineVariant = Color(0xFFB9C9C4),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFECF7F3),
    surfaceContainer = Color(0xFFE4F1ED),
    surfaceContainerHigh = Color(0xFFDEEBE7),
    surfaceContainerHighest = Color(0xFFD8E5E1),
    inverseSurface = Color(0xFF28322F),
    inverseOnSurface = Color(0xFFEAF2EF),
    inversePrimary = Color(0xFF5FD4C4),
    scrim = Color(0xFF000000),
)

internal val LettieDarkScheme = darkColorScheme(
    primary = Color(0xFF5FD4C4),
    onPrimary = Color(0xFF003731),
    primaryContainer = Color(0xFF005048),
    onPrimaryContainer = Color(0xFF7EE8D8),
    secondary = Color(0xFFA9CCC4),
    onSecondary = Color(0xFF103530),
    secondaryContainer = Color(0xFF294B46),
    onSecondaryContainer = Color(0xFFC5EDE5),
    tertiary = Color(0xFFFFB692),
    onTertiary = Color(0xFF5B1A00),
    tertiaryContainer = Color(0xFF803300),
    onTertiaryContainer = Color(0xFFFFDBCA),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0C1513),
    onBackground = Color(0xFFDCE5E1),
    surface = Color(0xFF0C1513),
    onSurface = Color(0xFFDCE5E1),
    surfaceVariant = Color(0xFF3A4A46),
    onSurfaceVariant = Color(0xFFB9C9C4),
    outline = Color(0xFF84948F),
    outlineVariant = Color(0xFF3A4A46),
    surfaceContainerLowest = Color(0xFF070D0C),
    surfaceContainerLow = Color(0xFF141D1B),
    surfaceContainer = Color(0xFF18211F),
    surfaceContainerHigh = Color(0xFF222B29),
    surfaceContainerHighest = Color(0xFF2C3633),
    inverseSurface = Color(0xFFDCE5E1),
    inverseOnSurface = Color(0xFF28322F),
    inversePrimary = Color(0xFF006B5F),
    scrim = Color(0xFF000000),
)
