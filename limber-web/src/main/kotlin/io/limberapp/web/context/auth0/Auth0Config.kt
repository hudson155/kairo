package io.limberapp.web.context.auth0

@Suppress("ConstructorParameterNaming") // These params exactly match what Auth0 expects.
internal data class Auth0Config(
    val domain: String,
    val client_id: String,
    val redirect_uri: String,
    val audience: String
)
