package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formTemplatesListPage

import react.RBuilder
import react.RProps
import react.child
import react.dom.h1
import react.dom.p
import react.functionalComponent

/**
 * Lists all form templates within the feature.
 */
internal fun RBuilder.formTemplatesListPage() {
    child(formTemplatesListPage)
}

private val formTemplatesListPage = functionalComponent<RProps> {
    h1 { +"Forms" }
    p { +"Here's a list of form templates." }
}
