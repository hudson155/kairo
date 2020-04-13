package io.limberapp.web

import io.limberapp.web.app.app
import io.limberapp.web.context.auth0.auth0Provider
import io.limberapp.web.context.globalState.stateProvider
import io.limberapp.web.util.AppState
import kotlinext.js.jsObject
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

private val onRedirectCallback: (AppState?) -> Unit = {
    window.history.replaceState(
        data = jsObject {},
        title = document.title,
        url = it?.targetUrl ?: window.location.pathname
    )
}

internal fun main() {
    render(document.getElementById("root")) {
        stateProvider {
            auth0Provider(onRedirectCallback = onRedirectCallback) {
                app()
            }
        }
    }
}
