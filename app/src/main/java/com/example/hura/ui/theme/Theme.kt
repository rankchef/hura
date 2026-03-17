// File: Theme.kt
package com.example.hura.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// -------------------
// Standard Material Color Schemes
// -------------------
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// -------------------
// Extended Colors
// -------------------
@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

@Immutable
data class ExtendedColorScheme(
    val income: ColorFamily,
    val expense: ColorFamily
)

// Example extended colors
val extendedLight = ExtendedColorScheme(
    income = ColorFamily(incomeLight, onIncomeLight, incomeContainerLight, onIncomeContainerLight),
    expense = ColorFamily(expenseLight, onExpenseLight, expenseContainerLight, onExpenseContainerLight)
)

val extendedDark = ExtendedColorScheme(
    income = ColorFamily(incomeDark, onIncomeDark, incomeContainerDark, onIncomeContainerDark),
    expense = ColorFamily(expenseDark, onExpenseDark, expenseContainerDark, onExpenseContainerDark)
)

// -------------------
// CompositionLocal for extended colors
// -------------------
private val LocalExtendedColors = staticCompositionLocalOf { extendedLight }

// -------------------
// MaterialTheme Extension Property
// -------------------
val MaterialTheme.extendedColors: ExtendedColorScheme
    @Composable
    get() = LocalExtendedColors.current

// -------------------
// HuraTheme Composable
// -------------------
@Composable
fun HuraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if (darkTheme) extendedDark else extendedLight

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}