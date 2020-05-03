package io.limberapp.web.app.components.footer

import io.limberapp.web.util.process
import kotlinx.css.Display
import kotlinx.css.display
import kotlinx.css.padding
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.dom.p
import react.dom.small
import react.functionalComponent
import styled.css
import styled.styledDiv
import kotlin.js.Date

/**
 * Bottom-of-page footer for use at the bottom of all pages.
 */
internal fun RBuilder.footer() {
    child(footer)
}

private val footer = functionalComponent<RProps> {
    styledDiv {
        css {
            display = Display.flex
            padding(vertical = 8.px, horizontal = 16.px)
        }
        p { small { +"Copyright © ${Date().getFullYear()} ${process.env.COPYRIGHT_HOLDER}" } }
    }
}
