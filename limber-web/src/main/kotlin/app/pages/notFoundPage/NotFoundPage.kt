package app.pages.notFoundPage

import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.styledH1
import styled.styledP

private val notFoundPage = functionalComponent<RProps> {
    styledH1 { +"Not Found" }
    styledP { +"We looked everywhere, but we couldn't find the page you were looking for." }
}

internal fun RBuilder.notFoundPage() {
    child(notFoundPage)
}
