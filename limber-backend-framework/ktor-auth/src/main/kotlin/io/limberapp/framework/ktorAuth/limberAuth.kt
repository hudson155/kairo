package io.limberapp.framework.ktorAuth

import io.ktor.auth.Authentication

fun Authentication.Configuration.limberAuth(name: String? = null, block: LimberAuthConfig.Builder.() -> Unit) {
    val config = LimberAuthConfig.Builder(name).apply(block)
    val provider = LimberAuthProvider(config.build())
    register(provider)
}
