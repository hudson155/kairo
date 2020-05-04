package io.limberapp.web.app.pages.notFoundPage

import react.RBuilder
import react.RProps
import react.child
import react.dom.h1
import react.dom.p
import react.functionalComponent

/**
 * "Not found" error message page when no page is found.
 */
internal fun RBuilder.notFoundPage() {
    child(notFoundPage)
}

private val notFoundPage = functionalComponent<RProps> {
    h1 { +"Not Found" }
    p { +"We looked everywhere, but we couldn't find the page you were looking for." }
}
