package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.components.formsFeatureSidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavLink.sidenavLink
import io.limberapp.web.app.components.sidenav.sidenav
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage.FORM_INSTANCES_LIST_PAGE_NAME
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesListPage.FORM_TEMPLATES_LIST_PAGE_NAME
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.useRouteMatch

/**
 * Sidenav for navigation within forms feature.
 */
internal fun RBuilder.formsFeatureSidenav() {
    child(formsFeatureSidenav)
}

private val formsFeatureSidenav = functionalComponent<RProps> {
    val routeMatch = useRouteMatch<RProps>()

    sidenav("Forms") {
        sidenavGroup {
            val root = checkNotNull(routeMatch).path
            sidenavLink(to = "$root/instances") { +FORM_INSTANCES_LIST_PAGE_NAME }
            sidenavLink(to = "$root/templates") { +FORM_TEMPLATES_LIST_PAGE_NAME }
        }
    }
}
