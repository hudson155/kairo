package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formInstancesListPage

import react.RBuilder
import react.RProps
import react.child
import react.dom.h1
import react.dom.p
import react.functionalComponent

/**
 * Lists all form instances within the feature.
 */
internal fun RBuilder.formInstancesListPage() {
    child(formInstancesListPage)
}

private val formInstancesListPage = functionalComponent<RProps> {
    h1 { +"Forms" }
    p { +"Here's a list of form instances." }
}
