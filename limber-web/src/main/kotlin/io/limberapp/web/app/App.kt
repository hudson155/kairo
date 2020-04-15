package io.limberapp.web.app

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.api.Fetch
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.featurePage.featurePage
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import io.limberapp.web.app.pages.signInPage.signInPage
import io.limberapp.web.app.pages.signOutPage.signOutPage
import io.limberapp.web.context.api.Api
import io.limberapp.web.context.api.apiProvider
import io.limberapp.web.context.auth0.authProvider
import io.limberapp.web.context.auth0.useAuth
import io.limberapp.web.context.globalState.action.tenant.TenantAction
import io.limberapp.web.context.globalState.globalStateProvider
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.AppState
import io.limberapp.web.util.async
import io.limberapp.web.util.rootDomain
import kotlinext.js.jsObject
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.browserRouter
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch
import react.useEffect
import kotlin.browser.document
import kotlin.browser.window

private val app = functionalComponent<RProps> {
    globalStateProvider {
        child(appWithAuth)
    }
}

internal fun RBuilder.app() {
    child(app)
}

private val onRedirectCallback: (AppState?) -> Unit = {
    window.history.replaceState(
        data = jsObject {},
        title = document.title,
        url = it?.targetUrl ?: window.location.pathname
    )
}

private val appWithAuth = functionalComponent<RProps> {

    val nonAuthenticatedApi = Api(Fetch())
    val global = useGlobalState()

    useEffect {
        if (global.state.tenant.hasBegunLoading) return@useEffect
        global.dispatch(TenantAction.BeginLoading)
        async {
            val tenant = nonAuthenticatedApi.tenants.get(rootDomain)
            global.dispatch(TenantAction.Set(tenant))
        }
    }

    if (!global.state.tenant.isLoaded) return@functionalComponent

    authProvider(
        clientId = checkNotNull(global.state.tenant.state).auth0ClientId,
        onRedirectCallback = onRedirectCallback
    ) {
        child(appWithApi)
    }
}

private val appWithApi = functionalComponent<RProps> {

    apiProvider {
        child(appRouter)
    }
}

private val appRouter = functionalComponent<RProps> {

    val auth = useAuth()

    if (auth.isLoading) return@functionalComponent

    browserRouter {
        switch {
            route(path = "/signin", exact = true) { buildElement { signInPage() } }
            route(path = "/signout", exact = true) { buildElement { signOutPage() } }
            route(path = "/") { buildElement { child(appFeatureRouter) } }
        }
    }
}

private val appFeatureRouter = functionalComponent<RProps> {

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

internal val List<FeatureRep.Complete>.default get() = singleOrNull { it.isDefaultFeature }
