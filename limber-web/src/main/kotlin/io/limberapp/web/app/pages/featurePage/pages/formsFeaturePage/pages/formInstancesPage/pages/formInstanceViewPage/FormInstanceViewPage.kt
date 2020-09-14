package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceViewPage

import io.limberapp.common.types.UUID
import io.limberapp.common.util.Outcome
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.web.api.load
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.formRenderer.formRenderer
import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.state.state.feature.useFeatureState
import io.limberapp.web.util.Page
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.formInstanceViewPage() {
  child(component)
}

internal typealias Props = RProps

internal object FormInstanceViewPage : Page {
  internal data class PageParams(val formInstanceGuid: UUID) : RProps

  fun path(featurePath: String, formInstanceGuid: UUID?) = listOf(
    featurePath,
    "instances",
    formInstanceGuid ?: ":${PageParams::formInstanceGuid.name}",
    "view",
  ).joinToString("/")
}

private class S : Styles("FormInstanceViewPage") {
  val footer by css {
    display = Display.flex
    flexDirection = FlexDirection.rowReverse
    paddingTop = 12.px
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val api = useApi()
  val match = checkNotNull(useRouteMatch<FormInstanceViewPage.PageParams>())

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
  div(classes = s.c { it::footer }) {
    +"Submitted at: "
    +checkNotNull(formInstance.submittedDate).toDateString()
  }
}
