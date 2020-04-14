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

// TODO: allow sub headers
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
    Styles.activeNavLink.invoke(CSSBuilder())

    styledDiv {
        css {
            +Styles.root
        }
        props.links.map {
            navLink(
                to = it.to,
                exact = true,
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
