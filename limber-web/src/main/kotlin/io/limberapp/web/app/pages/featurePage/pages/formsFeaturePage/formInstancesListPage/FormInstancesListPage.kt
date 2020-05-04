package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formInstancesListPage

import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.styledH1
import styled.styledP

/**
 * Lists all form instances within the feature.
 */
internal fun RBuilder.formInstancesListPage() {
    child(formInstancesListPage)
}

private val formInstancesListPage = functionalComponent<RProps> {
    styledH1 { +"Forms" }
    styledP { +"Here's a list of form instances." }
}
