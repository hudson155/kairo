package io.limberapp.web.app.components.navbar.components.headerLinkGroup

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

private val headerLinkGroup = functionalComponent<RProps> { props ->
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

internal fun RBuilder.headerLinkGroup(children: RHandler<RProps>) {
    child(headerLinkGroup, handler = children)
}
