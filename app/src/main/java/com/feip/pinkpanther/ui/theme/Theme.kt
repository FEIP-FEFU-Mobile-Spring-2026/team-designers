package com.feip.pinkpanther.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Фирменная цветовая схема Розовой Пантеры
private val DarkColorScheme = darkColorScheme(
    primary = PinkPanther,
    secondary = PinkPantherDark,
    tertiary = PinkPantherLight,
    background = PinkBackground,
    surface = PinkBackground,
    onPrimary = PinkBackground,
    onSecondary = PinkBackground,
    onTertiary = PinkBackground,
    onBackground = PinkText,
    onSurface = PinkText
)

private val LightColorScheme = lightColorScheme(
    primary = PinkPanther,
    secondary = PinkPantherDark,
    tertiary = PinkPantherLight,
    background = PinkBackground,
    surface = PinkBackground,
    onPrimary = PinkBackground,
    onSecondary = PinkBackground,
    onTertiary = PinkBackground,
    onBackground = PinkText,
    onSurface = PinkText
)

@Composable
fun PinkPantherStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Отключаем динамические цвета
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}