package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = QuranEmerald,
    onPrimary = QuranSurface,
    primaryContainer = QuranEmeraldContainer,
    onPrimaryContainer = QuranOnEmeraldContainer,
    secondary = QuranGold,
    onSecondary = QuranSurface,
    secondaryContainer = QuranGoldContainer,
    onSecondaryContainer = QuranOnGoldContainer,
    tertiary = QuranEmeraldLight,
    background = QuranBackground,
    surface = QuranSurface,
    surfaceVariant = QuranSurfaceVariant,
    onBackground = QuranTextPrimary,
    onSurface = QuranTextPrimary,
    onSurfaceVariant = QuranTextSecondary
)

private val DarkColorScheme = darkColorScheme(
    primary = QuranDarkPrimary,
    onPrimary = QuranDarkBackground,
    primaryContainer = QuranDarkSurfaceVariant,
    onPrimaryContainer = QuranDarkPrimary,
    secondary = QuranDarkGold,
    onSecondary = QuranDarkBackground,
    secondaryContainer = QuranDarkSurfaceVariant,
    onSecondaryContainer = QuranDarkGold,
    tertiary = QuranEmeraldLight,
    background = QuranDarkBackground,
    surface = QuranDarkSurface,
    surfaceVariant = QuranDarkSurfaceVariant,
    onBackground = QuranDarkTextPrimary,
    onSurface = QuranDarkTextPrimary,
    onSurfaceVariant = QuranDarkTextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
