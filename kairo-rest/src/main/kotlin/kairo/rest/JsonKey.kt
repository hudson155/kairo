package kairo.rest

import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import kairo.serialization.KairoJson

private val key: AttributeKey<KairoJson> = AttributeKey("json")

public var Application.json: KairoJson
  get() = attributes[key]
  set(value) {
    attributes[key] = value
  }
