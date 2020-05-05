package io.limberapp.web.context.theme

import kotlinx.css.Color

internal data class ThemeContext(
    val link: Color, // Color used as a background or text color to indicate a link.
    val smallActiveIndicator: Color, // Color used for small elements that indicate something is "active".
    val textLight: Color, // Standard light colored text.
    val textDark: Color, // Standard dark colored text.
    val backgroundDark: Color, // Standard black-ish background.
    val backgroundLight: Color, // Standard black-ish background.
    val backgroundDarkSubtleAccent: Color, // Subtle background used to indicate a light element is "active".
    val backgroundLightSubtleAccent: Color, // Subtle background used to indicate a dark element is "active".
    val backgroundDarkImportant: Color, // Subtle background used to indicate a light element is important.
    val backgroundLightImportant: Color, // Subtle background used to indicate a dark element is important
    val borderDark: Color, // Standard black-ish border. Lighter than backgroundDark.
    val borderLight: Color // Standard white-ish border. Darker than backgroundLight.
)
