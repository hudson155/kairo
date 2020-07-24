package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.pages.formTemplatesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.FormTemplatesPage
import io.limberapp.web.state.state.feature.useFeatureState
import react.*

internal fun RBuilder.formTemplatesListPage() {
  child(component)
}

internal typealias Props = RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (feature, _) = useFeatureState()
  layoutTitle("${feature.name} (${FormTemplatesPage.name.toLowerCase()})")
}
