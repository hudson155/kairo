package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.pages.formTemplatesListPage

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.FormTemplatesPage
import react.*

internal fun RBuilder.formTemplatesListPage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  layoutTitle("${props.feature.name} (${FormTemplatesPage.name.toLowerCase()})")
}
