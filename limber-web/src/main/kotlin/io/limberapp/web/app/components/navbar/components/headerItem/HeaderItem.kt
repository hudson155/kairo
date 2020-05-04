package io.limberapp.web.app.components.navbar.components.headerItem

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.alignItems
import kotlinx.css.color
import kotlinx.css.display
import kotlinx.css.marginRight
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.dom.b
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * A single item on a top-of-page navbar.
 */
internal fun RBuilder.headerItem(children: RHandler<RProps>) {
    child(headerItem, handler = children)
}

private val headerItem = functionalComponent<RProps> { props ->
    val theme = useTheme()

    styledDiv {
        css {
            display = Display.flex
            alignItems = Align.center
            marginRight = 16.px
            color = theme.textLight
        }
        b { props.children() }
    }
}
