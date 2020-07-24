package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.components.formsFeatureSidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavLink.sidenavLink
import io.limberapp.web.app.components.sidenav.sidenav
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.FormInstancesPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesPage.FormTemplatesPage
import react.*
import react.router.dom.*

internal fun RBuilder.formsFeatureSidenav(title: String) {
  child(component, Props(title))
}

internal data class Props(val title: String) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val match = checkNotNull(useRouteMatch<RProps>())

  sidenav(props.title) {
    sidenavGroup {
      sidenavLink(props.title, to = match.path + FormInstancesPage.subpath)
      sidenavLink(FormTemplatesPage.name, to = match.path + FormTemplatesPage.subpath)
    }
  }
}
