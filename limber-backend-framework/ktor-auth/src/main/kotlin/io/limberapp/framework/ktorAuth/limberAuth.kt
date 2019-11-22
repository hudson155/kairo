package io.limberapp.framework.ktorAuth

import io.ktor.auth.Authentication

fun Authentication.Configuration.limberAuth(
    name: String? = null,
    block: LimberAuthConfig.() -> Unit
) {
    val config = LimberAuthConfig(name).apply(block)
    val provider = LimberAuthProvider(config)
    register(provider)
}
