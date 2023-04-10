package com.example.composepractice.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.composepractice.R

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    surface = Blue,
    onSurface = Color.White,
    onPrimary = Color.Gray
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    surface = Blue,
    onSurface = Color.Gray,
    onPrimary = Color.DarkGray


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ComposePracticeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun ComposeTutorialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun BasicsCodelabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

private val BloomLightColorPaltte = lightColors(
    primary = pink100,
    secondary = pink900,
    background = white,
    surface = white850,
    onPrimary = gray,
    onSecondary = white,
    onBackground = gray,
    onSurface = gray,
)

private val BloomDarkColorPaltte = darkColors(
    primary = green900,
    secondary = green300,
    background = gray,
    surface = white150,
    onPrimary = white,
    onSecondary = gray,
    onBackground = white,
    onSurface = white850
)

open class WelcomeAssets(var background: Int, var illos: Int, var logo: Int)

object LightWelcomeAssets : WelcomeAssets(
    background = R.drawable.ic_light_welcome_bg,
    illos = R.drawable.ic_light_welcome_illos,
    logo = R.drawable.ic_light_logo
)

object DarkWelcomeAssets : WelcomeAssets(
    background = R.drawable.ic_dark_welcome_bg,
    illos = R.drawable.ic_dark_welcome_illos,
    logo = R.drawable.ic_dark_logo
)

internal var LocalWelcomeAssets = staticCompositionLocalOf { LightWelcomeAssets as WelcomeAssets }

val MaterialTheme.welcomeAssets
    @Composable
    @ReadOnlyComposable
    get() = LocalWelcomeAssets.current

enum class BloomTheme {
    LIGHT, DARK
}

@Composable
fun BloomTheme(theme: BloomTheme = BloomTheme.LIGHT, content: @Composable() () -> Unit) {
    val welcomeAssets = if (theme == BloomTheme.DARK) DarkWelcomeAssets else LightWelcomeAssets
    CompositionLocalProvider(
        LocalWelcomeAssets provides welcomeAssets,
    ) {
        MaterialTheme(
            colors = if (theme == BloomTheme.DARK) BloomDarkColorPaltte else BloomLightColorPaltte,
            typography = bloomTypoGraphy,
            shapes = shapes,
            content = content
        )
    }
}