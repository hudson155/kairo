package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.components.formsFeatureSidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavLink.sidenavLink
import io.limberapp.web.app.components.sidenav.sidenav
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.FormsFeaturePage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage.FormInstancesListPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesListPage.FormTemplatesListPage
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.useRouteMatch

/**
 * Sidenav for navigation within forms feature.
 */
internal fun RBuilder.formsFeatureSidenav() {
    child(component)
}

private val component = functionalComponent<RProps> {
    val match = checkNotNull(useRouteMatch<RProps>())

    sidenav(FormsFeaturePage.name) {
        sidenavGroup {
            sidenavLink(to = match.path + FormInstancesListPage.subpath) { +FormInstancesListPage.name }
            sidenavLink(to = match.path + FormTemplatesListPage.subpath) { +FormTemplatesListPage.name }
        }
    }
}
