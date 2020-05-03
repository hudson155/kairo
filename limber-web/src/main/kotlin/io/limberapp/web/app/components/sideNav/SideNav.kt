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
import styled.css
import styled.styledDiv

/**
 * Side nav for navigation within a feature.
 */
internal fun RBuilder.sideNav(children: RHandler<RProps> = {}) {
    child(sideNav, handler = children)
}

private val sideNav = functionalComponent<RProps> { props ->
    val theme = useTheme()

    styledDiv {
        // TODO (259): Improve default styling
        css {
            backgroundColor = theme.backgroundLight
            display = Display.flex
            flexDirection = FlexDirection.column
            height = LinearDimension.fitContent
            width = 150.px
        }
        props.children()
    }
}
