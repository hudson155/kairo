package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.pages.formTemplatesListPage

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.FormTemplatesPage
import io.limberapp.web.util.component
import react.*

/**
 * Lists all form templates within the feature.
 */
internal fun RBuilder.formTemplatesListPage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

private val component = component<Props> component@{ props ->
  layoutTitle("${props.feature.name} (${FormTemplatesPage.name.toLowerCase()})")
}
