package app.components.navbar.components.headerText

import kotlinx.css.Align
import kotlinx.css.Color
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
import react.functionalComponent
import styled.css
import styled.styledDiv

private val headerText = functionalComponent<RProps> { props ->
    styledDiv {
        css {
            display = Display.flex
            alignItems = Align.center
            marginRight = 16.px
            color = Color.white
        }
        props.children()
    }
}

internal fun RBuilder.headerText(children: RHandler<RProps>) {
    child(headerText, handler = children)
}
