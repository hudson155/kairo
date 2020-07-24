package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceCreationPage

import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.web.api.load
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.state.state.feature.useFeatureState
import io.limberapp.web.util.Page
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.formInstanceCreationPage() {
  child(component)
}

internal typealias Props = RProps

internal object FormInstanceCreationPage : Page {
  internal data class PageParams(val templateGuid: UUID) : RProps

  val subpath = "/create/:${PageParams::templateGuid.name}"
}

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()
  val match = checkNotNull(useRouteMatch<FormInstanceCreationPage.PageParams>())

  val (feature, _) = useFeatureState()

  val templateGuid = match.params.templateGuid

  val formTemplate = load { api(FormTemplateApi.Get(feature.guid, templateGuid)) }

  // While the form template is loading, show a loading spinner.
  (formTemplate ?: return centeredContentLayout { loadingSpinner(large = true) })
    .onFailure { return centeredContentLayout { failedToLoad("form template") } }

  div { p { +"Hey you are filling out template: ${formTemplate.value.title}" } }
}
