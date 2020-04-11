package app.components.footer

import kotlinx.css.Display
import kotlinx.css.display
import kotlinx.css.padding
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.dom.small
import react.functionalComponent
import styled.css
import styled.styledDiv
import styled.styledP
import kotlin.js.Date

private val footer = functionalComponent<RProps> {
    styledDiv {
        css { display = Display.flex; padding(16.px) }
        styledP { small { +"Copyright © ${Date().getFullYear()} Jeff Hudson" } }
    }
}

internal fun RBuilder.footer() {
    child(footer)
}
