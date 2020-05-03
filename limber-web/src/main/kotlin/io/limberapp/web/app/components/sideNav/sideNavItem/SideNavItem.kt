package io.limberapp.web.app.components.sideNav.sideNavItem

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.CSSBuilder
import kotlinx.css.PointerEvents
import kotlinx.css.backgroundColor
import kotlinx.css.color
import kotlinx.css.padding
import kotlinx.css.pointerEvents
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.navLink
import react.router.dom.useRouteMatch
import styled.StyleSheet
import styled.getClassName

/**
 * A single item on a side nav.
 */
internal fun RBuilder.sideNavItem(to: String, name: String) {
    child(sideNavItem, Props(to, name))
}

internal data class Props(val to: String, val name: String) : RProps

private val sideNavItem = functionalComponent<Props> { props ->
    val routeMatch = useRouteMatch<RProps>()
    val theme = useTheme()

    // TODO (259): Improve default styling
    val styles = object : StyleSheet("SideNavLinkStyles", isStatic = true) {
        val activeNavLink by css {
            backgroundColor = theme.backgroundAccent
            color = theme.textLight
            pointerEvents = PointerEvents.none
        }
        val navLink by css {
            hover {
                backgroundColor = theme.backgroundDark
                color = theme.textLight
            }
            padding(vertical = 4.px, horizontal = 8.px)
        }
    }

    // Manually adding the css to style sheet because classes are added by name, not invoked.
    // We should not have to do this elsewhere in the app unless we need to pass a className to a 3rd-party component.
    // Please don't copy this pattern unless you know what you're doing.
    // How to pass by class name: https://github.com/JetBrains/kotlin-wrappers/issues/179
    // Why pass by class name: https://github.com/JetBrains/kotlin-wrappers/issues/62
    styles.activeNavLink.invoke(CSSBuilder())
    styles.navLink.invoke(CSSBuilder())

    navLink<RProps>(
        to = "${routeMatch?.path.orEmpty()}${props.to}",
        exact = true,
        className = styles.getClassName { it::navLink },
        activeClassName = styles.getClassName { it::activeNavLink }
    ) { +props.name }
}
