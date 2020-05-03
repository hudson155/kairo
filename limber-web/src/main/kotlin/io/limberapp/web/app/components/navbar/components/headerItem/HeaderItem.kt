package io.limberapp.web.app.components.navbar.components.headerItem

import kotlinx.css.Align
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FontWeight
import kotlinx.css.alignItems
import kotlinx.css.color
import kotlinx.css.display
import kotlinx.css.fontWeight
import kotlinx.css.marginRight
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv

private val headerItem = functionalComponent<RProps> { props ->
    styledDiv {
        css {
            display = Display.flex
            alignItems = Align.center
            marginRight = 16.px
            color = Color.white
            fontWeight = FontWeight.bold
        }
        props.children()
    }
}

internal fun RBuilder.headerItem(children: RHandler<RProps>) {
    child(headerItem, handler = children)
}
