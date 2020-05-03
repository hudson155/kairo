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
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * A single item on a subnav.
 */
internal fun RBuilder.subnavItem(children: RHandler<RProps>) {
    child(subnavItem, handler = children)
}

private val subnavItem = functionalComponent<RProps> { props ->
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
            hover {
                color = theme.textLight
                backgroundColor = theme.backgroundAccent
            }
        }
        props.children()
    }
}
