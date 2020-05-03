package io.limberapp.web.app.components.navbar.components.subnav.components.subnavGroup

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.BorderStyle
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.borderBottomStyle
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * A group of items on a subnav. Items in the same group should be conceptually grouped. The physical spacing between
 * them will be less.
 */
internal fun RBuilder.subnavGroup(children: RHandler<RProps>) {
    child(subnavGroup, handler = children)
}

private val subnavGroup = functionalComponent<RProps> { props ->
    val theme = useTheme()

    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            borderBottom(1.px, BorderStyle.solid, theme.borderLight)
            lastOfType {
                borderBottomStyle = BorderStyle.none
            }
        }
        props.children()
    }
}
