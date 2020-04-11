package app.pages.featurePage.pages.formInstancesListPage

import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.styledH1
import styled.styledP

private val formInstancesListPage = functionalComponent<RProps> {
    styledH1 { +"Forms" }
    styledP { +"Here's a list of form instances." }
}

internal fun RBuilder.formInstancesListPage() {
    child(formInstancesListPage)
}
