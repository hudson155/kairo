package io.limberapp.web.app.components.navbar.components.subnav.components.subnavItem

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.color
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.marginBottom
import kotlinx.css.marginTop
import kotlinx.css.padding
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * A single item on a subnav. If [hoverable] is true, it will become accented when hovered.
 */
internal fun RBuilder.subnavItem(hoverable: Boolean = true, children: RHandler<Props>) {
    child(subnavItem, Props(hoverable), handler = children)
}

internal data class Props(val hoverable: Boolean) : RProps

private val subnavItem = functionalComponent<Props> { props ->
    val theme = useTheme()

    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.flexStart
            marginTop = 4.px
            padding(vertical = 4.px, horizontal = 8.px)
            lastOfType {
                marginBottom = 4.px
            }
            if (props.hoverable) {
                hover {
                    color = theme.textLight
                    backgroundColor = theme.link
                }
            }
        }
        props.children()
    }
}
