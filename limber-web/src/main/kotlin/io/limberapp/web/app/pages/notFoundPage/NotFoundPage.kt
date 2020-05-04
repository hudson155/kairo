package io.limberapp.web.app.pages.notFoundPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import kotlinx.css.TextAlign
import kotlinx.css.textAlign
import react.RBuilder
import react.RProps
import react.child
import react.dom.h1
import react.dom.p
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * "Not found" error message page when no page is found.
 */
internal fun RBuilder.notFoundPage() {
    child(notFoundPage)
}

private val notFoundPage = functionalComponent<RProps> {
    centeredContentLayout {
        styledDiv {
            css { textAlign = TextAlign.center }
            h1 { +"Not Found" }
            p { +"We looked everywhere, but we couldn't find the page you were looking for." }
        }
    }
}
