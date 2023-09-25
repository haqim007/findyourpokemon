package dev.haqim.findyourpokemon.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)

//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)

val LightColorScheme = lightColorScheme(
    primary = Red500,
    onPrimary = Color.White,
    primaryContainer = Red200,
    onPrimaryContainer = Color.Black,
    secondary = Yellow500,
    onSecondary = Color.Black,
    secondaryContainer = Yellow200,
    onSecondaryContainer = Color.Black,
    tertiary = Blue500,
    onTertiary = Color.White,
    tertiaryContainer = Blue200,
    onTertiaryContainer = Color.Black,
    surface = Blue200,
    onSurface = Color.White
)

val DarkColorScheme = darkColorScheme(
    primary = DarkRed500,
    onPrimary = Color.White,
    primaryContainer = DarkRed700,
    onPrimaryContainer= Color.White,
    secondary= DarkYellow500,
    onSecondary= Color.Black,
    secondaryContainer= DarkYellow700,
    onSecondaryContainer= Color.White,
    tertiary= DarkBlue500,
    onTertiary= Color.White,
    tertiaryContainer= DarkBlue700,
    onTertiaryContainer= Color.White,
    surface= DarkBlue700,
    onSurface= Color.White
)



@Composable
fun FindYourPokemonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}