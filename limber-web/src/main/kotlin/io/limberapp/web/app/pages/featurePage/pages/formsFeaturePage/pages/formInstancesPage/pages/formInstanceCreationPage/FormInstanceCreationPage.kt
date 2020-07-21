package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceCreationPage

import com.piperframework.types.UUID
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.action.formTemplates.loadFormTemplate
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.componentWithApi
import react.*
import react.dom.*
import react.router.dom.*

/**
 * Page for creating an instance of a template.
 */
internal fun RBuilder.formInstanceCreationPage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

internal object FormInstanceCreationPage {
  internal data class PageParams(val templateGuid: UUID) : RProps

  val subpath = "/create/:${PageParams::templateGuid.name}"
}

private val component = componentWithApi(RBuilder::component)
private fun RBuilder.component(self: ComponentWithApi, props: Props) {
  val match = checkNotNull(useRouteMatch<FormInstanceCreationPage.PageParams>())
  val templateGuid = match.params.templateGuid

  self.loadFormTemplate(props.feature.guid, templateGuid)

  val template = self.gs.formTemplates.completes[templateGuid].let { loadableState ->
    when (loadableState) {
      null, is LoadableState.Unloaded -> return centeredContentLayout { loadingSpinner(large = true) }
      is LoadableState.Error -> return centeredContentLayout { failedToLoad("form") }
      is LoadableState.Loaded -> return@let loadableState.state
    }
  }

  div { p { +"Hey you are filling out template: ${template.title}" } }
}
