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
  child(component)
}

internal object FormTemplatesListPage {
  const val name = "Templates"
  const val subpath = "/templates"
}

private val component = functionalComponent<RProps> {
  layoutTitle(FormTemplatesListPage.name)
}
