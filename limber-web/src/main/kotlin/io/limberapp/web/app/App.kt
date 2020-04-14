package io.limberapp.web.app

import io.limberapp.web.api.tenant.getTenant
import io.limberapp.web.context.auth0.authProvider
import io.limberapp.web.context.globalState.action.tenant.BeginLoadingTenant
import io.limberapp.web.context.globalState.action.tenant.SetTenant
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.AppState
import io.limberapp.web.util.async
import io.limberapp.web.util.rootDomain
import kotlinext.js.jsObject
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.useEffect
import kotlin.browser.document
import kotlin.browser.window

private val onRedirectCallback: (AppState?) -> Unit = {
    window.history.replaceState(
        data = jsObject {},
        title = document.title,
        url = it?.targetUrl ?: window.location.pathname
    )
}

private val app = functionalComponent<RProps> {
    val global = useGlobalState()
    useEffect {
        if (global.state.tenant.hasBegunLoading) return@useEffect
        global.dispatch(BeginLoadingTenant)
        async {
            val tenant = getTenant(rootDomain)
            global.dispatch(SetTenant(tenant))
        }
    }
    if (!global.state.tenant.isLoaded) return@functionalComponent
    authProvider(
        clientId = checkNotNull(global.state.tenant.state).auth0ClientId,
        onRedirectCallback = onRedirectCallback
    ) {
        appWithGlobalState()
    }
}

internal fun RBuilder.app() {
    child(app)
}
