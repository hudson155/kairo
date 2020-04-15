package io.limberapp.web.app.components.sideNav

import io.limberapp.web.context.theme.ThemeContext
import kotlinx.css.CSSBuilder
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.LinearDimension
import kotlinx.css.PointerEvents
import kotlinx.css.backgroundColor
import kotlinx.css.color
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.height
import kotlinx.css.padding
import kotlinx.css.pointerEvents
import kotlinx.css.px
import kotlinx.css.width
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.navLink
import react.useContext
import styled.StyleSheet
import styled.css
import styled.getClassName
import styled.styledDiv

// TODO (259): allow sub headers
internal data class NavLink(val to: String, val name: String)

internal data class Props(val links: List<NavLink>) : RProps

private val sideNav = functionalComponent<Props> { props ->
    val theme = useContext(ThemeContext)
    console.log("theme", theme);

    // TODO (259): Improve default css'ing
    class Styles : StyleSheet("SideNavBarStyles", isStatic = true) {
        val navLink by css {
            padding = "4px 8px 4px 8px"
            hover {
                backgroundColor = theme.backgroundDark
                color = theme.white
            }
        }
        val activeNavLink by css {
            backgroundColor = theme.backgroundAccent
            color = theme.white
            pointerEvents = PointerEvents.none
        }
        val root by css {
            backgroundColor = theme.backgroundLight
            width = 150.px
            display = Display.flex
            flexDirection = FlexDirection.column
            height = LinearDimension.fitContent
        }
    }
    val styles = Styles();

    // Manually add the css
    styles.activeNavLink.invoke(CSSBuilder())
    styles.navLink.invoke(CSSBuilder())

    styledDiv {
        css {
            +styles.root
        }
        props.links.map {
            navLink(
                to = it.to,
                exact = true,
                className = styles.getClassName { s -> s::navLink },
                activeClassName = styles.getClassName { s -> s::activeNavLink }
            ) {
                +it.name
            }
        }
    }
}

internal fun RBuilder.sideNav(links: List<NavLink>) {
    child(sideNav, Props(links))
}
