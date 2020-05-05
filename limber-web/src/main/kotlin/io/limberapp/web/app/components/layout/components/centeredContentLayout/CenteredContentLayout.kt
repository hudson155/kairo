package io.limberapp.web.app.components.layout.components.centeredContentLayout

import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.alignItems
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.flexGrow
import kotlinx.css.justifyContent
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
 * A layout that supports a single element or vertical group of elements, centered on the page both vertically and
 * horizontally. Content is provided by [children]. Content should be kept small; behaviour is undefined upon overflow.
 */
internal fun RBuilder.centeredContentLayout(children: RHandler<RProps>) {
    child(centeredContentLayout, handler = children)
}

private val centeredContentLayout = functionalComponent<RProps> { props ->
    styledDiv {
        css {
            flexGrow = 1.0
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
            justifyContent = JustifyContent.center
            margin(16.px)
        }
        props.children()
    }
}
