package io.limberapp.web.app.components.sidenav.components.sidenavLink

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.Align
import kotlinx.css.BorderStyle
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
import styled.getClassName

/**
 * A single link on a sidenav.
 */
internal fun RBuilder.sidenavLink(to: String, children: RHandler<Props>) {
    child(sidenavLink, Props(to), handler = children)
}

internal data class Props(val to: String) : RProps

private val styles = object : Styles("SidenavLink") {
    val navLink by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        alignItems = Align.flexStart
        color = Theme.link
        backgroundColor = Theme.backgroundLight
        padding(8.px)
        borderLeft(2.px, BorderStyle.solid, Color.transparent)
        borderBottom(1.px, BorderStyle.solid, Theme.borderLight)
        lastOfType {
            borderBottomStyle = BorderStyle.none
        }
        hover {
            backgroundColor = Theme.backgroundLightSubtleAccent
        }
    }
    val activeNavLink by css {
        borderLeftColor = Theme.smallActiveIndicator
    }
}.apply { inject() }

private val sidenavLink = functionalComponent<Props> { props ->
    navLink<RProps>(
        to = props.to,
        exact = true,
        className = styles.getClassName { it::navLink },
        activeClassName = styles.getClassName { it::activeNavLink }) {
        props.children()
    }
}
