package io.limberapp.web.app.components.sideNav

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.LinearDimension
import kotlinx.css.backgroundColor
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.height
import kotlinx.css.px
import kotlinx.css.width
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent
import styled.StyleSheet
import styled.css
import styled.styledDiv

private val sideNav = functionalComponent<RProps> { props ->
    val theme = useTheme()

    // TODO (259): Improve default css'ing
    val styles = object : StyleSheet("SideNavBarStyles", isStatic = true) {
        val root by css {
            backgroundColor = theme.backgroundLight
            display = Display.flex
            flexDirection = FlexDirection.column
            height = LinearDimension.fitContent
            width = 150.px
        }
    }

    styledDiv {
        css { +styles.root }
        props.children()
    }
}

internal fun RBuilder.sideNav(children: RHandler<RProps> = {}) {
    child(sideNav, handler = children)
}
