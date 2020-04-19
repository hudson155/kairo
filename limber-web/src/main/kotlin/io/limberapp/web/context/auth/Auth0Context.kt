package io.limberapp.web.context.auth

internal data class Auth0Context(
    val isLoading: Boolean,
    val isAuthenticated: Boolean,
    val signIn: () -> Unit,
    val jwt: Jwt?,
    val signOut: () -> Unit
)
