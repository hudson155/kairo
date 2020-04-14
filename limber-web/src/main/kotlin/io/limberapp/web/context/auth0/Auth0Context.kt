package io.limberapp.web.context.auth0

internal data class Auth0Context(
    val isLoading: Boolean,
    val isAuthenticated: Boolean,
    val signIn: () -> Unit,
    val getJwt: suspend () -> String,
    val signOut: () -> Unit
)
