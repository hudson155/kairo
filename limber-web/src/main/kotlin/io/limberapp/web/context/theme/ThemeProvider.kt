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

private val defaultTheme = ThemeContext(
    backgroundAccent = Color("#1098F7"),
    backgroundDark = Color("#24292E"),
    backgroundLight = Color("#f1f1f1"),
    white = Color("#FDFFFC")
)

internal data class Props(val theme: ThemeContext) : RProps

private val themeProvider = functionalComponent<Props> { props ->
    val configObject = ProviderValue(props.theme)
    child(createElement(themeContext.Provider, configObject, props.children))
}

internal fun RBuilder.themeProvider(theme: ThemeContext = defaultTheme, children: RHandler<Props>) {
    child(themeProvider, Props(theme), handler = children)
}

