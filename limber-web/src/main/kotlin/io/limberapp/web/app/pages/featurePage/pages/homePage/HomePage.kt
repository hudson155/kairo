package io.limberapp.web.app.pages.featurePage.pages.homePage

import react.RBuilder
import react.RProps
import react.child
import react.dom.h1
import react.dom.p
import react.functionalComponent

/**
 * Placeholder homepage.
 */
internal fun RBuilder.homePage() {
    child(homePage)
}

private val homePage = functionalComponent<RProps> {
    h1 { +"Home" }
    p { +"This is the homepage." }
}
