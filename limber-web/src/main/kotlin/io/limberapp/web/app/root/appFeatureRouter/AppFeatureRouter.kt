package io.limberapp.web.app.root.appFeatureRouter

import io.limberapp.backend.module.orgs.rep.org.default
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.mainAppNavbar.mainAppNavbar
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.featurePage.featurePage
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.orgSettingsPage
import io.limberapp.web.state.state.orgState.useOrgState
import io.limberapp.web.util.rootPath
import react.*
import react.router.dom.*

/**
 * Handles routing for the authenticated portion of the application.
 */
internal fun RBuilder.appFeatureRouter() {
  child(component)
}

internal typealias Props = RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (org, _) = useOrgState()

  val features = org.features

  page(header = buildElement { mainAppNavbar() }, footer = buildElement { footer() }) {
    switch {
      features.default?.let { route(path = rootPath, exact = true) { redirect(to = it.path) } }
      route(path = OrgSettingsPage.path) { buildElement { orgSettingsPage() } }
      features.forEach { feature ->
        route(path = feature.path) { buildElement { featurePage(feature) } }
      }
      route(path = rootPath) { buildElement { notFoundPage() } }
    }
  }
}
