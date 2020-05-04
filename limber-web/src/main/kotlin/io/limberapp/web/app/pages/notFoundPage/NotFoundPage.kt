package io.limberapp.web.app.pages.notFoundPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import react.RBuilder
import react.RProps
import react.child
import react.dom.p
import react.functionalComponent

/**
 * "Not found" error message page when no page is found.
 */
internal fun RBuilder.notFoundPage() {
    child(notFoundPage)
}

private val notFoundPage = functionalComponent<RProps> {
    standardLayout {
        layoutTitle("Not Found")
        p { +"We looked everywhere, but we couldn't find the page you were looking for." }
    }
}
