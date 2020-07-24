package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.web.api.load
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.FormInstancesPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.formInstancesPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.FormTemplatesPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.formTemplatesPage
import io.limberapp.web.state.state.feature.useFeatureState
import io.limberapp.web.state.state.formTemplates.formTemplatesStateProvider
import react.*
import react.router.dom.*

internal fun RBuilder.formsFeaturePage() {
  child(component)
}

internal typealias Props = RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()
  val match = checkNotNull(useRouteMatch<RProps>())

  val (feature, _) = useFeatureState()

  val formTemplates = load { api(FormTemplateApi.GetByFeatureGuid(feature.guid)) }

  // TODO: The line below is temporarily commented out to disable form templates.
  //  It'll likely be re-enabled as part of the "#475 Form builder" epic.
//  standardLayout(leftPane = buildElement { formsFeatureSidenav(props.feature.name) }) {
  standardLayout {
    // While the form templates are loading, show a loading spinner.
    (formTemplates ?: return@standardLayout loadingSpinner())
      .onFailure { return@standardLayout failedToLoad("form templates") }

    formTemplatesStateProvider(formTemplates = formTemplates.value.associateBy { it.guid }) {
      switch {
        route(path = match.path, exact = true) {
          redirect(to = match.path + FormInstancesPage.subpath)
        }
        route(path = match.path + FormInstancesPage.subpath) {
          buildElement { formInstancesPage() }
        }
        route(path = match.path + FormTemplatesPage.subpath, exact = true) {
          buildElement { formTemplatesPage() }
        }
      }
    }
  }
}
