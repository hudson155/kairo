package io.limberapp.web.context.auth0

import kotlin.js.Promise

internal data class Auth0Context(
    val isLoading: Boolean,
    val isAuthenticated: Boolean,
    val signIn: () -> Unit,
    val getJwt: () -> Promise<String>,
    val signOut: () -> Unit
)
