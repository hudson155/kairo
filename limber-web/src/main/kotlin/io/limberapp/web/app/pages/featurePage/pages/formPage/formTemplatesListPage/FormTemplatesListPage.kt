package io.limberapp.web.app.pages.featurePage.pages.formPage.formTemplatesListPage

import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.styledH1
import styled.styledP

private val formTemplatesListPage = functionalComponent<RProps> {
    styledH1 { +"Forms" }
    styledP { +"Here's a list of form templates." }
}

internal fun RBuilder.formTemplatesListPage() {
    child(formTemplatesListPage)
}
