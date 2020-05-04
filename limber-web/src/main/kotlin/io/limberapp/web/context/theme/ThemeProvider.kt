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

private val themeContext = createContext<ThemeContext>();
internal fun useTheme() = useContext(themeContext)

private val defaultTheme = run {
    // https://coolors.co/303030-fcfcfc-c49890-2898f0-606470
    val jet = Color("#303030")
    val jetLighter20 = Color("#636363")
    val babyPowder = Color("#FCFCFC")
    val babyPowderDarker20 = Color("#C9C9C9")
    val tuscany = Color("#C49890")
    val dodgerBlue = Color("#2898F0")
    val blackCoral = Color("#606470")

    return@run ThemeContext(
        textDark = jet,
        textLight = babyPowder,
        backgroundDark = jet,
        backgroundLight = babyPowder,
        backgroundAccent = dodgerBlue,
        borderDark = jetLighter20,
        borderLight = babyPowderDarker20
    )
}

internal data class Props(val theme: ThemeContext) : RProps

private val themeProvider = functionalComponent<Props> { props ->
    val configObject = ProviderValue(props.theme)
    child(createElement(themeContext.Provider, configObject, props.children))
}

internal fun RBuilder.themeProvider(theme: ThemeContext = defaultTheme, children: RHandler<Props>) {
    child(themeProvider, Props(theme), handler = children)
}

