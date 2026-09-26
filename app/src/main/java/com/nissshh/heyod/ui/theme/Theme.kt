package com.nissshh.heyod.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AmoledDarkColorScheme = darkColorScheme(
    primary = Color(0xFFE11D48),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF330510),
    onPrimaryContainer = Color(0xFFFFD9DF),
    secondary = Color(0xFF38BDF8),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF0C3547),
    onSecondaryContainer = Color(0xFFBAE6FD),
    background = Color(0xFF000000), // Pure OLED pitch black
    onBackground = Color(0xFFEDEDED),
    surface = Color(0xFF0A0A0C),    // Deep sleek AMOLED surface
    onSurface = Color(0xFFEDEDED),
    surfaceVariant = Color(0xFF141418),
    onSurfaceVariant = Color(0xFFA1A1AA),
    outline = Color(0xFF27272A)
)

@Composable
fun HeyODTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AmoledDarkColorScheme,
        content = content
    )
}
