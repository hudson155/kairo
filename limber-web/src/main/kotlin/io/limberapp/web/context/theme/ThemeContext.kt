package io.limberapp.web.context.theme

import kotlinx.css.Color

internal data class ThemeContext(
    val textLight: Color, // Standard light colored text.
    val textDark: Color, // Standard dark colored text.
    val backgroundDark: Color, // Standard black-ish background.
    val backgroundLight: Color, // Standard black-ish background.
    val backgroundAccent: Color, // Background used to indicate an element is "active".
    val borderDark: Color, // Standard black-ish border. Lighter than backgroundDark.
    val borderLight: Color // Standard white-ish border. Darker than backgroundLight.
)
