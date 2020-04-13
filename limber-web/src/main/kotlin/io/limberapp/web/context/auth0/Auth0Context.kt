package io.limberapp.web.context.auth0

internal data class Auth0Context(
    val isLoading: Boolean,
    val isAuthenticated: Boolean,
    val login: () -> Unit,
    val getJwt: suspend () -> String,
    val logout: () -> Unit
)
