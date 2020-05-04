package io.limberapp.web.app.pages.notFoundPage

import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.styledH1
import styled.styledP

/**
 * "Not found" error message page when no page is found.
 */
internal fun RBuilder.notFoundPage() {
    child(notFoundPage)
}

private val notFoundPage = functionalComponent<RProps> {
    styledH1 { +"Not Found" }
    styledP { +"We looked everywhere, but we couldn't find the page you were looking for." }
}
