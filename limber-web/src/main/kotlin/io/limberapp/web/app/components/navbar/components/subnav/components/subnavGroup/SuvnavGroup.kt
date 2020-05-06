package io.limberapp.web.app.components.navbar.components.subnav.components.subnavGroup

import io.limberapp.web.util.Theme
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
 * them will be less for items in the same group than for items in different groups.
 */
internal fun RBuilder.subnavGroup(children: RHandler<RProps>) {
    child(subnavGroup, handler = children)
}

private val subnavGroup = functionalComponent<RProps> { props ->
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            borderBottom(1.px, BorderStyle.solid, Theme.borderLight)
            lastOfType {
                borderBottomStyle = BorderStyle.none
            }
        }
        props.children()
    }
}
