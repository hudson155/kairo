package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.util.Page
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Lists all form instances within the feature.
 */
internal fun RBuilder.formInstancesListPage() {
    child(component)
}

internal val formInstancesListPage = Page(
    name = "Instances",
    path = "/instances"
)

private val component = functionalComponent<RProps> {
    layoutTitle(formInstancesListPage.path)
}
