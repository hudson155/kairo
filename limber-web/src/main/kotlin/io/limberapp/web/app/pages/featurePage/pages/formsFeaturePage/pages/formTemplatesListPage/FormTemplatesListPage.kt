package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.util.Page
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Lists all form templates within the feature.
 */
internal fun RBuilder.formTemplatesListPage() {
    child(component)
}

internal val formTemplatesListPage = Page(
    name = "Templates",
    path = "/templates"
)

private val component = functionalComponent<RProps> {
    layoutTitle(formTemplatesListPage.name)
}
