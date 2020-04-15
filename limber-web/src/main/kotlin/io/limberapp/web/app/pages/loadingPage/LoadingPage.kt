package io.limberapp.web.app.pages.loadingPage

import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.alignItems
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.flexGrow
import kotlinx.css.justifyContent
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent
import styled.css
import styled.styledDiv
import styled.styledH1
import styled.styledP

internal data class Props(val loadingText: String) : RProps

private val loadingPage = functionalComponent<Props> { props ->
    styledDiv {
        css {
            flexGrow = 1.toDouble()
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
            justifyContent = JustifyContent.center
        }
        div(classes = "lds-hourglass") {}
        styledP { +props.loadingText }
    }
}

internal fun RBuilder.loadingPage(loadingText: String) {
    child(loadingPage, Props(loadingText))
}
