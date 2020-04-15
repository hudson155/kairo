package io.limberapp.web.context.theme

import kotlinx.css.Color
import react.createContext

internal data class ThemeContextType(
    val backgroundDark: Color,
    val backgroundAccent: Color,
    val backgroundLight: Color,
    val white: Color
)

internal val DefaultTheme = ThemeContextType(
    backgroundDark = Color("#24292E"),
    backgroundAccent = Color("#1098F7"),
    backgroundLight = Color("#f1f1f1"),
    white = Color("#FDFFFC")
)

internal val ThemeContext = createContext<ThemeContextType>();

