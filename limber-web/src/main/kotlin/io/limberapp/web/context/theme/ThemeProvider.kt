package io.limberapp.web.context.theme

import io.limberapp.web.context.ProviderValue
import kotlinx.css.Color
import react.RBuilder
import react.RHandler
import react.RProps
import react.children
import react.createContext
import react.createElement
import react.functionalComponent
import react.useContext

internal fun RBuilder.themeProvider(theme: ThemeContext = defaultTheme, children: RHandler<Props>) {
    child(themeProvider, Props(theme), handler = children)
}

private val themeContext = createContext<ThemeContext>();
internal fun useTheme() = useContext(themeContext)

@Suppress("UnusedPrivateMember")
private val defaultTheme = run {
    // https://coolors.co/303030-fcfcfc-c49890-1078d8-606470
    val jet = Color("#303030")
    val jetLighter5 = Color("#3D3D3D")
    val jetLighter10 = Color("#4A4A4A")
    val jetLighter20 = Color("#636363")
    val babyPowder = Color("#FCFCFC")
    val babyPowderDarker5 = Color("#EFEFEF")
    val babyPowderDarker10 = Color("#E3E3E3")
    val babyPowderDarker20 = Color("#C9C9C9")
    val copperCrayola = Color("#CC8C70")
    val brightNavyBlue = Color("#1078D8")
    val blackCoral = Color("#606470")

    return@run ThemeContext(
        link = brightNavyBlue,
        smallActiveIndicator = copperCrayola,
        textDark = jet,
        textLight = babyPowder,
        backgroundDark = jet,
        backgroundLight = babyPowder,
        backgroundDarkSubtleAccent = jetLighter5,
        backgroundLightSubtleAccent = babyPowderDarker5,
        backgroundDarkImportant = jetLighter10,
        backgroundLightImportant = babyPowderDarker10,
        borderDark = jetLighter20,
        borderLight = babyPowderDarker20
    )
}

internal data class Props(val theme: ThemeContext) : RProps

private val themeProvider = functionalComponent<Props> { props ->
    val configObject = ProviderValue(props.theme)
    child(createElement(themeContext.Provider, configObject, props.children))
}

