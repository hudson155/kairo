package io.limberapp.web.app.components.sideNav.sideNavLink

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.CSSBuilder
import kotlinx.css.PointerEvents
import kotlinx.css.backgroundColor
import kotlinx.css.color
import kotlinx.css.padding
import kotlinx.css.pointerEvents
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.navLink
import styled.StyleSheet
import styled.getClassName

internal data class Props(val to: String, val name: String) : RProps

private val sideNavLink = functionalComponent<Props> { props ->
    val theme = useTheme()

    // TODO (259): Improve default css'ing
    val styles = object : StyleSheet("SideNavBarStyles", isStatic = true) {
        val activeNavLink by css {
            backgroundColor = theme.backgroundAccent
            color = theme.white
            pointerEvents = PointerEvents.none
        }
        val navLink by css {
            hover {
                backgroundColor = theme.backgroundDark
                color = theme.white
            }
            padding = "4px 8px 4px 8px"
        }
    }

    // Manually add the css to style sheet
    // https://github.com/JetBrains/kotlin-wrappers/issues/179
    styles.activeNavLink.invoke(CSSBuilder())
    styles.navLink.invoke(CSSBuilder())

    navLink(
        to = props.to,
        exact = true,
        className = styles.getClassName { it::navLink },
        activeClassName = styles.getClassName { it::activeNavLink }
    ) { +props.name }
}

internal fun RBuilder.sideNavLink(to: String, name: String) {
    child(sideNavLink, Props(to, name))
}
