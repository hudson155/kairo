package com.piperframework.ktorAuth

import io.ktor.auth.Authentication
import io.ktor.auth.Principal

fun <P : Principal> Authentication.Configuration.piperAuth(
  name: String? = null,
  block: PiperAuthConfig.Builder<P>.() -> Unit
) {
  val config = PiperAuthConfig.Builder<P>(name).apply(block)
  val provider = PiperAuthProvider(config.build())
  register(provider)
}
