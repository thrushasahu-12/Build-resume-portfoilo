package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CyanPrimary,
    onPrimary = Color(0xFF00363F),
    primaryContainer = Color(0xFF004E5B),
    onPrimaryContainer = CyanLight,

    secondary = EmeraldAccent,
    onSecondary = Color(0xFF003824),
    secondaryContainer = Color(0xFF005236),
    onSecondaryContainer = EmeraldLight,

    tertiary = PurpleAccent,
    onTertiary = Color(0xFF2E1568),
    tertiaryContainer = Color(0xFF452C7F),
    onTertiaryContainer = PurpleLight,

    background = DarkBg,
    onBackground = TextPrimaryDark,

    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondaryDark,

    outline = DarkBorder,
    outlineVariant = DarkBorderGlow
)

private val LightColorScheme = lightColorScheme(
    primary = CyanDark,
    onPrimary = Color.White,
    primaryContainer = CyanLight,
    onPrimaryContainer = Color(0xFF001F26),

    secondary = EmeraldDark,
    onSecondary = Color.White,
    secondaryContainer = EmeraldLight,
    onSecondaryContainer = Color(0xFF002113),

    tertiary = PurpleAccent,
    onTertiary = Color.White,
    tertiaryContainer = PurpleLight,
    onTertiaryContainer = Color(0xFF1F084D),

    background = LightBg,
    onBackground = TextPrimaryLight,

    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = TextSecondaryLight,

    outline = LightBorder,
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek executive dark theme for high-impact animations
    dynamicColor: Boolean = false, // Keep intentional brand identity for consistent glow animations
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
