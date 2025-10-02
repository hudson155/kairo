package kairo.rest.auth

import io.ktor.server.application.Application
import io.ktor.util.AttributeKey

private val key: AttributeKey<AuthConfig> = AttributeKey("authConfig")

public var Application.authConfig: AuthConfig?
  get() = attributes.getOrNull(key)
  set(value) {
    value?.let { attributes[key] = value } ?: attributes.remove(key)
  }
