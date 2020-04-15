package io.limberapp.web.app.components.sideNav

import io.limberapp.web.app.components.ColorConsts
import kotlinx.css.BorderStyle
import kotlinx.css.CSSBuilder
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.LinearDimension
import kotlinx.css.PointerEvents
import kotlinx.css.backgroundColor
import kotlinx.css.borderColor
import kotlinx.css.borderRadius
import kotlinx.css.borderStyle
import kotlinx.css.borderWidth
import kotlinx.css.color
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.height
import kotlinx.css.padding
import kotlinx.css.pointerEvents
import kotlinx.css.properties.border
import kotlinx.css.px
import kotlinx.css.width
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.navLink
import styled.StyleSheet
import styled.css
import styled.getClassName
import styled.styledDiv

// TODO (259): allow sub headers
internal data class NavLink(val to: String, val name: String)

internal data class Props(val links: List<NavLink>) : RProps

// TODO (259): Improve default css'ing
private object Styles : StyleSheet("SideNavBarStyles", isStatic = true) {
    val navLink by css {
        padding = "4px 8px 4px 8px"
        hover {
            backgroundColor = ColorConsts.gray
            color = ColorConsts.white
        }
    }
    val activeNavLink by css {
        backgroundColor = ColorConsts.blue
        color = ColorConsts.white
        pointerEvents = PointerEvents.none
    }
    val root by css {
        backgroundColor = ColorConsts.lightGray
        width = 150.px
        display = Display.flex
        flexDirection = FlexDirection.column
        height = LinearDimension.fitContent
    }
}

private val sideNav = functionalComponent<Props> { props ->
    // Manually add the css
    Styles.activeNavLink.invoke(CSSBuilder())
    Styles.navLink.invoke(CSSBuilder())

    styledDiv {
        css {
            +Styles.root
        }
        props.links.map {
            navLink(
                to = it.to,
                exact = true,
                className = Styles.getClassName { it::navLink },
                activeClassName = Styles.getClassName { it::activeNavLink }
            ) {
                +it.name
            }
        }
    }
}

internal fun RBuilder.sideNav(links: List<NavLink>) {
    child(sideNav, Props(links))
}
