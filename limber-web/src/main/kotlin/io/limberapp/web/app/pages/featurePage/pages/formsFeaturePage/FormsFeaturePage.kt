package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage

import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.components.formsFeatureSidenav.formsFeatureSidenav
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesListPage.formInstancesListPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formTemplatesListPage.formTemplatesListPage
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch
import react.router.dom.useRouteMatch

/**
 * Parent page for forms module pages.
 */
internal fun RBuilder.formsFeaturePage() {
    child(component)
}

private val component = functionalComponent<RProps> {
    val routeMatch = useRouteMatch<RProps>()

    standardLayout(leftPane = buildElement { formsFeatureSidenav() }) {
        switch {
            val root = checkNotNull(routeMatch).path
            route(path = root, exact = true) {
                redirect(to = root + formInstancesListPage.path)
            }
            route(path = root + formInstancesListPage.path, exact = true) {
                buildElement { formInstancesListPage() }
            }
            route(path = root + formTemplatesListPage.path, exact = true) {
                buildElement { formTemplatesListPage() }
            }
        }
    }
}
