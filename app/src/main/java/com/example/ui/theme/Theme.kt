package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = AgriGreenLight,
    onPrimary = Color(0xFF003921),
    primaryContainer = AgriGreenDark,
    onPrimaryContainer = AgriGreenContainer,
    secondary = AgriHarvestGold,
    onSecondary = Color(0xFF422B00),
    secondaryContainer = Color(0xFF5E3F00),
    onSecondaryContainer = AgriGoldContainer,
    tertiary = AgriSkyBlue,
    onTertiary = Color.White,
    background = AgriBackgroundDark,
    onBackground = AgriOnSurfaceDark,
    surface = AgriSurfaceDark,
    onSurface = AgriOnSurfaceDark,
    surfaceVariant = AgriSurfaceVariantDark,
    onSurfaceVariant = Color(0xFFC0CEC4),
    outline = AgriOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = AgriGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = AgriGreenContainer,
    onPrimaryContainer = AgriOnGreenContainer,
    secondary = AgriHarvestGold,
    onSecondary = Color.White,
    secondaryContainer = AgriGoldContainer,
    onSecondaryContainer = Color(0xFF422B00),
    tertiary = AgriSkyBlue,
    onTertiary = Color.White,
    tertiaryContainer = AgriSkyContainer,
    onTertiaryContainer = Color(0xFF00202E),
    background = AgriBackgroundLight,
    onBackground = AgriOnSurfaceLight,
    surface = AgriSurfaceLight,
    onSurface = AgriOnSurfaceLight,
    surfaceVariant = AgriSurfaceVariantLight,
    onSurfaceVariant = Color(0xFF404D44),
    outline = AgriOutlineLight
)

val AgriShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted Agricultural design system
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AgriShapes,
        content = content
    )
}
