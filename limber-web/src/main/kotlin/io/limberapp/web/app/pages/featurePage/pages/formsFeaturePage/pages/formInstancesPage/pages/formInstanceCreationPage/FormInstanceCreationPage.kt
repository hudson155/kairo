package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceCreationPage

import com.piperframework.types.UUID
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

private val component = componentWithApi<RProps> component@{ _, _ ->
  val match = checkNotNull(useRouteMatch<FormInstanceCreationPage.PageParams>())
  val templateGuid = match.params.templateGuid

  div { p { +"Hey you are filling out template with id: $templateGuid" } }
}
