package io.limberapp.web.app

import com.piperframework.restInterface.Fetch
import io.limberapp.web.app.components.basicNavbar.basicNavbar
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.context.api.Api
import io.limberapp.web.context.api.json
import io.limberapp.web.context.auth.authProvider
import io.limberapp.web.context.globalState.action.tenant.ensureTenantLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.Theme
import io.limberapp.web.util.external.AppState
import io.limberapp.web.util.process
import io.limberapp.web.util.rootDomain
import io.limberapp.web.util.withContext
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
    child(component)
}

private val onRedirectCallback: (AppState?) -> Unit = {
    window.history.replaceState(
        data = jsObject {},
        title = document.title,
        url = it?.targetUrl ?: window.location.pathname
    )
}

private val component = functionalComponent<RProps> {
    // We use a non-authenticated API here rather than calling the useApi() hook which we should do everywhere else
    // because the tenant must be fetched before we can create the AuthProvider, and the AuthProvider is required for
    // the ApiProvider.
    val nonAuthenticatedApi = Api(Fetch(process.env.API_ROOT_URL, json))
    val global = useGlobalState()

    // Set theme elements
    useEffect(listOf(Theme.Color.Background.light)) {
        checkNotNull(document.body).style.color = Theme.Color.Text.dark.value
        checkNotNull(document.body).style.backgroundColor = Theme.Color.Background.light.value
    }

    withContext(global, nonAuthenticatedApi) { ensureTenantLoaded(rootDomain) }

    // While the tenant is loading, show the loading page.
    val tenant = global.state.tenant.let { loadableState ->
        if (!loadableState.isLoaded) {
            page(header = buildElement { basicNavbar() }, footer = buildElement { footer() }) {
                loadingPage("Loading tenant...")
            }
            return@functionalComponent
        }
        return@let checkNotNull(loadableState.state)
    }

    authProvider(
        clientId = tenant.auth0ClientId,
        onRedirectCallback = onRedirectCallback
    ) {
        appWithApi()
    }
}
