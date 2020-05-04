package io.limberapp.web.app

import com.piperframework.restInterface.Fetch
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.web.app.components.basicNavbar.basicNavbar
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.context.api.Api
import io.limberapp.web.context.auth.authProvider
import io.limberapp.web.context.globalState.action.tenant.TenantAction
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.context.theme.useTheme
import io.limberapp.web.util.async
import io.limberapp.web.util.external.AppState
import io.limberapp.web.util.process
import io.limberapp.web.util.rootDomain
import kotlinext.js.jsObject
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.useEffect
import kotlin.browser.document
import kotlin.browser.window

/**
 * Part of the application root.
 *   - Loads the tenant.
 *   - Provides auth, but doesn't guarantee that auth is loaded.
 */
internal fun RBuilder.appWithAuth() {
    child(appWithAuth)
}

private val onRedirectCallback: (AppState?) -> Unit = {
    window.history.replaceState(
        data = jsObject {},
        title = document.title,
        url = it?.targetUrl ?: window.location.pathname
    )
}

private val appWithAuth = functionalComponent<RProps> {
    // We use a non-authenticated API here rather than calling the useApi() hook which we should do everywhere else
    // because the tenant must be fetched before we can create the AuthProvider, and the AuthProvider is required for
    // the ApiProvider.
    val nonAuthenticatedApi = Api(Fetch(process.env.API_ROOT_URL))
    val global = useGlobalState()
    val theme = useTheme()

    // Set theme elements
    useEffect(listOf(theme.backgroundLight)) {
        checkNotNull(document.body).style.color = theme.textDark.value
        checkNotNull(document.body).style.backgroundColor = theme.backgroundLight.value
    }

    // Load tenant asynchronously.
    useEffect(emptyList()) {
        if (global.state.tenant.hasBegunLoading) return@useEffect
        global.dispatch(TenantAction.BeginLoading)
        async {
            val tenant = nonAuthenticatedApi.tenants(TenantApi.GetByDomain(rootDomain))
            global.dispatch(TenantAction.Set(tenant))
        }
    }

    // While the tenant is loading, show the loading page.
    if (!global.state.tenant.isLoaded) {
        page(header = buildElement { basicNavbar() }, footer = buildElement { footer() }) {
            loadingPage("Loading tenant...")
        }
        return@functionalComponent
    }

    authProvider(
        clientId = checkNotNull(global.state.tenant.state).auth0ClientId,
        onRedirectCallback = onRedirectCallback
    ) {
        appWithApi()
    }
}
