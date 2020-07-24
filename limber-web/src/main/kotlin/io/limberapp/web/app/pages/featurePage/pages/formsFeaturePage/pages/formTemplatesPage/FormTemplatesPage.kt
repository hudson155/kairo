package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.pages.formTemplatesListPage.formTemplatesListPage
import io.limberapp.web.util.Page
import react.*
import react.router.dom.*

internal fun RBuilder.formTemplatesPage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

internal object FormTemplatesPage : Page {
  const val name = "Templates"
  const val subpath = "/templates"
}

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val match = checkNotNull(useRouteMatch<RProps>())

  switch {
    route(path = match.path, exact = true) {
      buildElement { formTemplatesListPage(props.feature) }
    }
  }
}
