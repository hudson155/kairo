package io.limberapp.web.app.components.sidenav.components.sidenavGroup

import io.limberapp.web.util.Theme
import kotlinx.css.BorderStyle
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.Overflow
import kotlinx.css.borderRadius
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.marginBottom
import kotlinx.css.overflow
import kotlinx.css.properties.border
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * A group of items on a sidenav. Items in the same group should be conceptually grouped. The physical spacing between
 * them will be less for items in the same group than for items in different groups.
 */
internal fun RBuilder.sidenavGroup(children: RHandler<RProps>) {
    child(sidenavGroup, handler = children)
}

private val sidenavGroup = functionalComponent<RProps> { props ->
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            border(1.px, BorderStyle.solid, Theme.borderLight)
            borderRadius = 4.px
            marginBottom = 16.px
            overflow = Overflow.hidden // Avoid background color overflow.
            lastOfType {
                marginBottom = 0.px
            }
        }
        props.children()
    }
}
