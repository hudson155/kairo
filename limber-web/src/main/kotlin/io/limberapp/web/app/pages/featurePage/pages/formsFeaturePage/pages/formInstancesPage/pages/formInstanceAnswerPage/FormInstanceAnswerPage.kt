package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceAnswerPage

import com.piperframework.types.UUID
import com.piperframework.util.Outcome
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.web.api.load
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.formInstanceRenderer.formRenderer
import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.state.state.feature.useFeatureState
import io.limberapp.web.util.Page
import react.*
import react.router.dom.*

internal fun RBuilder.formInstanceAnswerPage() {
  child(component)
}

internal typealias Props = RProps

internal object FormInstanceAnswerPage : Page {
  internal data class PageParams(val formInstanceGuid: UUID) : RProps

  // TODO ENG-43: Potentially convert this to an absolute path function
  val subpath = "/:${PageParams::formInstanceGuid.name}/edit"
}

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val api = useApi()
  val match = checkNotNull(useRouteMatch<FormInstanceAnswerPage.PageParams>())

  val (feature, _) = useFeatureState()

  val formInstanceGuid = match.params.formInstanceGuid

  val form = load {
    api(FormInstanceApi.Get(feature.guid, formInstanceGuid))
      .map { formInstance ->
        api(FormTemplateApi.Get(feature.guid, formInstance.formTemplateGuid))
          .map { formInstanceTemplate -> Outcome.Success(Pair(formInstance, formInstanceTemplate)) }
      }
  } ?: return loadingSpinner()

  form.onFailure { return failedToLoad("form") }
  val (formInstance, formTemplate) = form.value

  layoutTitle(formTemplate.title)
  formRenderer(formTemplate.questions, formInstance)
}
