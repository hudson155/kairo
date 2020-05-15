package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage

import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.components.formsFeatureSidenav.formsFeatureSidenav
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage.FormInstancesListPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage.formInstancesListPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesListPage.FormTemplatesListPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesListPage.formTemplatesListPage
import react.*
import react.router.dom.*

/**
 * Parent page for forms module pages.
 */
internal fun RBuilder.formsFeaturePage() {
  child(component)
}

internal object FormsFeaturePage {
  const val name = "Forms"
}

private val component = functionalComponent<RProps> {
  val match = checkNotNull(useRouteMatch<RProps>())

  standardLayout(leftPane = buildElement { formsFeatureSidenav() }) {
    switch {
      route(path = match.path, exact = true) {
        redirect(to = match.path + FormInstancesListPage.subpath)
      }
      route(path = match.path + FormInstancesListPage.subpath, exact = true) {
        buildElement { formInstancesListPage() }
      }
      route(path = match.path + FormTemplatesListPage.subpath, exact = true) {
        buildElement { formTemplatesListPage() }
      }
    }
  }
}
