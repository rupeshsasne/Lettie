package com.radix2.llm.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import lastlettermaster.shared.generated.resources.Res
import lastlettermaster.shared.generated.resources.fredoka_bold
import lastlettermaster.shared.generated.resources.fredoka_medium
import lastlettermaster.shared.generated.resources.fredoka_regular
import lastlettermaster.shared.generated.resources.fredoka_semibold
import org.jetbrains.compose.resources.Font

/**
 * Rounded Fredoka type — matches the pillowy letterforms in the Lettie mark.
 * Display/headline stay bold & large for early readers; body stays open.
 */
@Composable
fun rememberLettieTypography(): Typography {
    val family = rememberLettieFontFamily()
    return remember(family) {
        Typography(
            displayLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Bold,
                fontSize = 57.sp,
                lineHeight = 64.sp,
                letterSpacing = (-0.25).sp,
            ),
            displayMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Bold,
                fontSize = 45.sp,
                lineHeight = 52.sp,
            ),
            displaySmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 46.sp,
            ),
            headlineLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                lineHeight = 40.sp,
            ),
            headlineMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 34.sp,
            ),
            headlineSmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 30.sp,
            ),
            titleLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 30.sp,
            ),
            titleMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.1.sp,
            ),
            titleSmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.1.sp,
            ),
            bodyLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.2.sp,
            ),
            bodyMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.2.sp,
            ),
            bodySmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
            ),
            labelLarge = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.1.sp,
            ),
            labelMedium = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.3.sp,
            ),
            labelSmall = TextStyle(
                fontFamily = family,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
        )
    }
}

@Composable
fun rememberLettieFontFamily(): FontFamily {
    val regular = Font(Res.font.fredoka_regular, weight = FontWeight.Normal)
    val medium = Font(Res.font.fredoka_medium, weight = FontWeight.Medium)
    val semibold = Font(Res.font.fredoka_semibold, weight = FontWeight.SemiBold)
    val bold = Font(Res.font.fredoka_bold, weight = FontWeight.Bold)
    return remember(regular, medium, semibold, bold) {
        FontFamily(regular, medium, semibold, bold)
    }
}
