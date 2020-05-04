package io.limberapp.web.app.components.navbar.components.headerGroup

import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.alignItems
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.margin
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * A group of items on a top-of-page navbar. Items in the same group should be conceptually grouped. The physical
 * spacing between them will be less for items in the same group than for items in different groups.
 */
internal fun RBuilder.headerGroup(children: RHandler<RProps>) {
    child(headerGroup, handler = children)
}

private val headerGroup = functionalComponent<RProps> { props ->
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.row
            alignItems = Align.center
            margin(vertical = 0.px, horizontal = 16.px)
        }
        props.children()
    }
}
