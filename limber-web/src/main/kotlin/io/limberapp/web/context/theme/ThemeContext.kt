package io.limberapp.web.context.theme

import kotlinx.css.Color
import react.createContext

internal data class ThemeContextType(
    val backgroundAccent: Color,
    val backgroundDark: Color,
    val backgroundLight: Color,
    val white: Color
)

internal val DefaultTheme = ThemeContextType(
    backgroundAccent = Color("#1098F7"),
    backgroundDark = Color("#24292E"),
    backgroundLight = Color("#f1f1f1"),
    white = Color("#FDFFFC")
)

internal val ThemeContext = createContext<ThemeContextType>();

