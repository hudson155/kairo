package io.limberapp.web.app.pages.featurePage.pages.homePage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import react.RBuilder
import react.RProps
import react.child
import react.dom.p
import react.functionalComponent

/**
 * Placeholder homepage.
 */
internal fun RBuilder.homePage() {
    child(homePage)
}

private val homePage = functionalComponent<RProps> {
    standardLayout {
        layoutTitle("Home")
        p { +"This is the homepage." }
    }
}
