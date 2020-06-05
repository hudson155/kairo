package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage

import com.piperframework.types.UUID
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage.components.formInstancesTable.formInstancesTable
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.action.formInstances.loadFormInstances
import io.limberapp.web.util.componentWithApi
import react.*

/**
 * Lists all form instances within the feature.
 */
internal fun RBuilder.formInstancesListPage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

internal object FormInstancesListPage {
  const val subpath = "/instances"
}

private val component = componentWithApi<Props> component@{ self, props ->
  self.loadFormInstances(props.feature.guid)

  layoutTitle(props.feature.name)

  // While the form instances are loading, show a loading spinner.
  val formInstances = self.gs.formInstances[props.feature.guid].let { loadableState ->
    when (loadableState) {
      null, is LoadableState.Unloaded -> return@component loadingSpinner()
      is LoadableState.Error -> return@component failedToLoad("forms")
      is LoadableState.Loaded -> return@let loadableState.state.values.toSet()
    }
  }

  formInstancesTable(formInstances)
}
