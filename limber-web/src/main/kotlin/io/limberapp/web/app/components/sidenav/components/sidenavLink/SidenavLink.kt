package io.limberapp.web.app.components.sidenav.components.sidenavLink

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.CSSBuilder
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.borderBottomStyle
import kotlinx.css.borderLeftColor
import kotlinx.css.color
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.padding
import kotlinx.css.properties.borderBottom
import kotlinx.css.properties.borderLeft
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.functionalComponent
import react.router.dom.navLink
import react.useEffect
import styled.StyleSheet
import styled.getClassName

/**
 * A single link on a sidenav.
 */
internal fun RBuilder.sidenavLink(to: String, children: RHandler<Props>) {
    child(sidenavLink, Props(to), handler = children)
}

internal data class Props(val to: String) : RProps

private var stylesSet = false

private val sidenavLink = functionalComponent<Props> { props ->
    val theme = useTheme()

    val styles = object : StyleSheet("SideNavLinkStyles", isStatic = true) {
        val navLink by css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.flexStart
            color = theme.link
            backgroundColor = theme.backgroundLight
            padding(8.px)
            borderLeft(2.px, BorderStyle.solid, Color.transparent)
            borderBottom(1.px, BorderStyle.solid, theme.borderLight)
            lastOfType {
                borderBottomStyle = BorderStyle.none
            }
            hover {
                backgroundColor = theme.backgroundLightSubtleAccent
            }
        }
        val activeNavLink by css {
            borderLeftColor = theme.smallActiveIndicator
        }
    }

    useEffect(listOf(styles)) {
        if (stylesSet) return@useEffect
        // Manually adding the css to style sheet because classes are added by name, not invoked.
        // We should not have to do this elsewhere in the app
        // unless we need to pass a className to a 3rd-party component.
        // Please don't copy this pattern unless you know what you're doing.
        // How to pass by class name: https://github.com/JetBrains/kotlin-wrappers/issues/179
        // Why pass by class name: https://github.com/JetBrains/kotlin-wrappers/issues/62
        styles.activeNavLink.invoke(CSSBuilder())
        styles.navLink.invoke(CSSBuilder())
        stylesSet = true
    }

    navLink<RProps>(
        to = props.to,
        exact = true,
        className = styles.getClassName { it::navLink },
        activeClassName = styles.getClassName { it::activeNavLink }) {
        props.children()
    }
}
