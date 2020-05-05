package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Lists all form templates within the feature.
 */
internal fun RBuilder.formTemplatesListPage() {
    child(formTemplatesListPage)
}

internal const val FORM_TEMPLATES_LIST_PAGE_NAME = "Templates"

private val formTemplatesListPage = functionalComponent<RProps> {
    layoutTitle(FORM_TEMPLATES_LIST_PAGE_NAME)
}
