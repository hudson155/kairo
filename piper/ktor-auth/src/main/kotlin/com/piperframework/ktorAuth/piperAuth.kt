package com.piperframework.ktorAuth

import io.ktor.auth.Authentication

fun Authentication.Configuration.piperAuth(name: String? = null, block: PiperAuthConfig.Builder.() -> Unit) {
    val config = PiperAuthConfig.Builder(name).apply(block)
    val provider = PiperAuthProvider(config.build())
    register(provider)
}
