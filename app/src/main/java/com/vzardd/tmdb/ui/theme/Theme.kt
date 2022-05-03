package com.vzardd.tmdb.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = DarkBlue,
    primaryVariant = LightBlue,
    secondary = LightGreen,
    secondaryVariant = Color.DarkGray,

    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.LightGray,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

private val LightColorPalette = lightColors(
    primary = DarkBlue,
    primaryVariant = LightBlue,
    secondary = LightGreen,
    secondaryVariant = Color.DarkGray,


    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.LightGray,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun TheMovieDBTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ){
        content()
    }
}