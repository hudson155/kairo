package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.components.formsFeatureSidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavLink.sidenavLink
import io.limberapp.web.app.components.sidenav.sidenav
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage.formInstancesListPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesListPage.formTemplatesListPage
import io.limberapp.web.util.Strings
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
    val routeMatch = useRouteMatch<RProps>()

    sidenav(Strings.formsTitle) {
        sidenavGroup {
            val root = checkNotNull(routeMatch).path
            sidenavLink(to = root + formInstancesListPage.path) { +formInstancesListPage.name }
            sidenavLink(to = root + formTemplatesListPage.path) { +formTemplatesListPage.name }
        }
    }
}
