package com.feip.pinkpanther.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PinkColorScheme = lightColorScheme(
    primary = PinkPanther,
    onPrimary = PinkBackground,
    primaryContainer = PinkPantherLight,
    onPrimaryContainer = PinkPantherDark,
    secondary = PinkPantherDark,
    onSecondary = PinkBackground,
    secondaryContainer = PinkPantherLight,
    onSecondaryContainer = PinkText,
    tertiary = PinkPantherLight,
    onTertiary = PinkText,
    background = PinkBackground,
    onBackground = PinkText,
    surface = PinkBackground,
    onSurface = PinkText,
    surfaceVariant = PinkPantherLight.copy(alpha = 0.3f),
    onSurfaceVariant = PinkPantherDark,
    outline = PinkPanther.copy(alpha = 0.5f),
    outlineVariant = PinkPantherLight.copy(alpha = 0.5f)
)

@Composable
fun PinkPantherTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PinkColorScheme,
        typography = Typography,
        content = content
    )
    }