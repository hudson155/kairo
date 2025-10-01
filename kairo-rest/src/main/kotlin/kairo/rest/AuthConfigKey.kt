package kairo.rest

import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import kairo.rest.auth.AuthConfig

private val key: AttributeKey<AuthConfig> = AttributeKey("authConfig")

public var Application.authConfig: AuthConfig?
  get() = attributes.getOrNull(key)
  set(value) {
    value?.let { attributes[key] = value } ?: attributes.remove(key)
  }
