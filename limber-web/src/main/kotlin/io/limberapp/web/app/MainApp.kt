package io.limberapp.web.app

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.api.tenant.getTenant
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.featurePage.featurePage
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import io.limberapp.web.context.globalState.action.tenant.BeginLoadingTenant
import io.limberapp.web.context.globalState.action.tenant.SetTenant
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.async
import io.limberapp.web.util.rootDomain
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch
import react.useEffect

private val mainApp = functionalComponent<RProps> {
    val global = useGlobalState()
    val features = global.state.org.state!!.features.toList()
    page(header = buildElement { navbar(features, "Firstname Lastname") }, footer = buildElement { footer() }) {
        switch {
            features.default?.let { route(path = "/", exact = true) { redirect(from = "/", to = it.path) } }
            features.map { feature ->
                route(path = feature.path, exact = true) { buildElement { featurePage(feature) } }
            }
            route(path = "/") { buildElement { notFoundPage() } }
        }
    }
}

internal fun RBuilder.mainApp() {
    child(mainApp)
}

internal val List<FeatureRep.Complete>.default get() = singleOrNull { it.isDefaultFeature }
