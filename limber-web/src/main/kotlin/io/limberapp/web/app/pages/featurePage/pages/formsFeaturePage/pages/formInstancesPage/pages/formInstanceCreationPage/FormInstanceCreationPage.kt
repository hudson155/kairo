package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceCreationPage

import com.piperframework.types.UUID
import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.action.formTemplates.loadFormTemplate
import io.limberapp.web.util.componentWithApi
import react.*
import react.dom.*
import react.router.dom.*

/**
 * Page for creating an instance of a template.
 */
internal fun RBuilder.formInstanceCreationPage() {
  child(component)
}

internal object FormInstanceCreationPage {
  internal data class PageParams(val templateGuid: UUID) : RProps

  val subpath = "/create/:${PageParams::templateGuid.name}"
}

private val component = componentWithApi<RProps> component@{ self, _ ->
  val match = checkNotNull(useRouteMatch<FormInstanceCreationPage.PageParams>())
  val templateGuid = match.params.templateGuid

  self.loadFormTemplate(templateGuid)

  val template = self.gs.formTemplates.completes[templateGuid].let { loadableState ->
    when (loadableState) {
      null, is LoadableState.Unloaded -> return@component centeredContentLayout { loadingSpinner(large = true) }
      is LoadableState.Error -> return@component centeredContentLayout { failedToLoad("form") }
      is LoadableState.Loaded -> return@let loadableState.state
    }
  }

  div { p { +"Hey you are filling out template: ${template.title}" } }
}
