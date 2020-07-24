package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage.components.formInstancesTable.formInstancesTable
import io.limberapp.web.state.state.feature.useFeatureState
import io.limberapp.web.state.state.formInstances.useFormInstancesState
import react.*

internal fun RBuilder.formInstancesListPage() {
  child(component)
}

internal typealias Props = RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (feature, _) = useFeatureState()
  val (formInstances, _) = useFormInstancesState()

  layoutTitle(feature.name)

  formInstancesTable(formInstances.values.toSet())
}
