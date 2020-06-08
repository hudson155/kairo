package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.pages.formTemplatesListPage.formTemplatesListPage
import io.limberapp.web.util.component
import react.*
import react.router.dom.*

/**
 * Parent page for form template pages.
 */
internal fun RBuilder.formTemplatesPage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

internal object FormTemplatesPage {
  const val name = "Templates"
  const val subpath = "/templates"
}

private val component = component<Props> component@{ props ->
  val match = checkNotNull(useRouteMatch<RProps>())

  switch {
    route(path = match.path, exact = true) {
      buildElement { formTemplatesListPage(props.feature) }
    }
  }
}
