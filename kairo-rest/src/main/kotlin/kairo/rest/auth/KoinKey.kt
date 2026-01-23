package kairo.rest.auth

import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import org.koin.core.Koin

private val key: AttributeKey<Koin> = AttributeKey("koin")

public var Application.koin: Koin
  get() = attributes[key]
  set(value) {
    attributes[key] = value
  }
