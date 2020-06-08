package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.FormInstancesPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.formInstancesPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.FormTemplatesPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.formTemplatesPage
import io.limberapp.web.util.component
import react.*
import react.router.dom.*

/**
 * Parent page for forms module pages.
 */
internal fun RBuilder.formsFeaturePage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

private val component = component<Props> component@{ props ->
  val match = checkNotNull(useRouteMatch<RProps>())

  // TODO: The line below is temporarily commented out to disable form templates.
  //  It'll likely be re-enabled as part of the "#475 Form builder" epic.
//  standardLayout(leftPane = buildElement { formsFeatureSidenav(props.feature.name) }) {
  standardLayout {
    switch {
      route(path = match.path, exact = true) {
        redirect(to = match.path + FormInstancesPage.subpath)
      }
      route(path = match.path + FormInstancesPage.subpath) {
        buildElement { formInstancesPage(props.feature) }
      }
      route(path = match.path + FormTemplatesPage.subpath, exact = true) {
        buildElement { formTemplatesPage(props.feature) }
      }
    }
  }
}
