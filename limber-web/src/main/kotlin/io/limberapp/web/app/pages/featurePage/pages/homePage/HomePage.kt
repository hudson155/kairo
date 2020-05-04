package io.limberapp.web.app.pages.featurePage.pages.homePage

import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.styledH1
import styled.styledP

/**
 * Placeholder homepage.
 */
internal fun RBuilder.homePage() {
    child(homePage)
}

private val homePage = functionalComponent<RProps> {
    styledH1 { +"Home" }
    styledP { +"This is the homepage." }
}
