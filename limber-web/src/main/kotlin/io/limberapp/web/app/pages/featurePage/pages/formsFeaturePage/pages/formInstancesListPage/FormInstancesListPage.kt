package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.util.component
import react.*

/**
 * Lists all form instances within the feature.
 */
internal fun RBuilder.formInstancesListPage() {
  child(component)
}

internal object FormInstancesListPage {
  const val name = "Instances"
  const val subpath = "/instances"
}

private val component = component<RProps> component@{ _ ->
  layoutTitle(FormInstancesListPage.name)
}
