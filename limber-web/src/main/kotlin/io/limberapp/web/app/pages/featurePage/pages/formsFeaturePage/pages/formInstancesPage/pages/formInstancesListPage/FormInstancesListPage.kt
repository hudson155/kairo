package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.api.load
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage.components.formInstancesTable.formInstancesTable
import react.*

internal fun RBuilder.formInstancesListPage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()

  val formInstances = load { api(FormInstanceApi.GetByFeatureGuid(props.feature.guid)) }
  val formTemplates = load { api(FormTemplateApi.GetByFeatureGuid(props.feature.guid)) }

  layoutTitle(props.feature.name)

  // While the form instances are loading, show a loading spinner.
  if (formInstances == null) return loadingSpinner()
  formInstances.onFailure { return failedToLoad("form instances") }

  // While the form templates are loading, show a loading spinner.
  if (formTemplates == null) return loadingSpinner()
  formTemplates.onFailure { return failedToLoad("form templates") }

  formInstancesTable(
    formInstances = formInstances.value,
    formTemplates = formTemplates.value.associateBy { it.guid }
  )
}
