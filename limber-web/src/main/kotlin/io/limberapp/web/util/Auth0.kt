package io.limberapp.web.util

import kotlinext.js.JsObject
import kotlin.js.Promise

internal external interface Auth0Client {
    fun handleRedirectCallback(): Promise<RedirectCallbackResponse>
    fun isAuthenticated(): Promise<Boolean>
    fun loginWithRedirect()
    fun getTokenSilently(): Promise<String>
    fun logout(request: JsObject)
}

internal external interface AppState {
    val targetUrl: String?
}

internal external interface RedirectCallbackResponse {
    val appState: AppState?
}

data class Auth0LogoutRequestProps(val returnTo: String)

@JsModule("@auth0/auth0-spa-js")
@JsNonModule
internal external fun createAuth0Client(auth0Config: JsObject): Promise<Auth0Client>
