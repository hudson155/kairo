package io.limberapp.web.app.components.sideNav

import kotlinx.css.CSSBuilder
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.backgroundColor
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.padding
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

internal data class NavLink(val to: String, val name: String)

internal data class Props(val links: List<NavLink>) : RProps

private object Styles : StyleSheet("FormPageStyles", isStatic = true) {
    val activeNavLink by css {
        backgroundColor = Color.gray
    }
    val root by css {
        backgroundColor = Color.lightGray
        width = 200.px
        display = Display.flex
        flexDirection = FlexDirection.column
        padding = "6px"
    }
}

private val sideNav = functionalComponent<Props> { props ->
    // Manually add the css
    // TODO move this somewhere
    Styles.activeNavLink.invoke(CSSBuilder())

    styledDiv {
        css {
            +Styles.root
        }
        navLink(
            to = "/forms/",
            exact = true,
            activeClassName = Styles.getClassName { it::activeNavLink }
        ) {
            +"Home"
        }
        navLink(
            to = "/forms/templates",
            activeClassName = Styles.getClassName { it::activeNavLink }
        ) {
            +"Templates"
        }
        navLink(
            to = "/forms/instances",
            activeClassName = Styles.getClassName { it::activeNavLink }
        ) {
            +"Insantes"
        }
    }
}

internal fun RBuilder.sideNav(links: List<NavLink>) {
    child(sideNav, Props(links))
}
