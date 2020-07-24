package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage

import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceCreationPage.FormInstanceCreationPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceCreationPage.formInstanceCreationPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage.formInstancesListPage
import io.limberapp.web.util.Page
import react.*
import react.router.dom.*

internal fun RBuilder.formInstancesPage() {
  child(component)
}

internal typealias Props = RProps

internal object FormInstancesPage : Page {
  const val subpath = "/instances"
}

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val match = checkNotNull(useRouteMatch<RProps>())

  switch {
    route(path = match.path, exact = true) {
      buildElement { formInstancesListPage() }
    }
    route(path = match.path + FormInstanceCreationPage.subpath, exact = true) {
      buildElement { formInstanceCreationPage() }
    }
  }
}
