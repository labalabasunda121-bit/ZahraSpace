package com.zahra.space.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val LightColors = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = AccentMint,
    onPrimaryContainer = PrimaryDarkGreen,
    secondary = PrimaryLightGreen,
    onSecondary = Color.White,
    secondaryContainer = AccentMint,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark,
    error = ErrorRed
)
private val DarkColors = darkColorScheme(
    primary = PrimaryLightGreen,
    onPrimary = Color.Black,
    primaryContainer = PrimaryDarkGreen,
    onPrimaryContainer = AccentMint,
    secondary = PrimaryGreen,
    onSecondary = Color.Black,
    secondaryContainer = PrimaryDarkGreen,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)
@Composable
fun ZahraSpaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colorScheme, typography = AppTypography, content = content)
}
