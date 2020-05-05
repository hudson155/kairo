package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Lists all form instances within the feature.
 */
internal fun RBuilder.formInstancesListPage() {
    child(formInstancesListPage)
}

internal const val FORM_INSTANCES_LIST_PAGE_NAME = "Instances"

private val formInstancesListPage = functionalComponent<RProps> {
    layoutTitle(FORM_INSTANCES_LIST_PAGE_NAME)
}
