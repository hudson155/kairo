package io.limberapp.web.util

import kotlinx.css.Color

@Suppress("UnusedPrivateMember")
internal object Theme {
    // https://coolors.co/303030-fcfcfc-c49890-1078d8-606470
    private val jet = Color("#303030")
    private val jetLighter5 = Color("#3D3D3D")
    private val jetLighter10 = Color("#4A4A4A")
    private val jetLighter20 = Color("#636363")
    private val babyPowder = Color("#FCFCFC")
    private val babyPowderDarker5 = Color("#EFEFEF")
    private val babyPowderDarker10 = Color("#E3E3E3")
    private val babyPowderDarker20 = Color("#C9C9C9")
    private val copperCrayola = Color("#CC8C70")
    private val brightNavyBlue = Color("#1078D8")
    private val blackCoral = Color("#606470")

    // Color used as a background or text color to indicate a link.
    val link = brightNavyBlue

    // Color used for small elements that indicate something is "active".
    val smallActiveIndicator = copperCrayola

    // Standard dark colored text.
    val textDark = jet

    // Standard light colored text.
    val textLight = babyPowder

    // Standard black-ish background.
    val backgroundDark = jet

    // Standard black-ish background.
    val backgroundLight = babyPowder

    // Subtle background used to indicate a light element is "active".
    val backgroundDarkSubtleAccent = jetLighter5

    // Subtle background used to indicate a dark element is "active".
    val backgroundLightSubtleAccent = babyPowderDarker5

    // Subtle background used to indicate a light element is important.
    val backgroundDarkImportant = jetLighter10

    // Subtle background used to indicate a dark element is important
    val backgroundLightImportant = babyPowderDarker10

    // Standard black-ish border. Lighter than backgroundDark.
    val borderDark = jetLighter20

    // Standard white-ish border. Darker than backgroundLight.
    val borderLight = babyPowderDarker20
}
