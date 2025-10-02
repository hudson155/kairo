package kairo.rest

import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json

private val key: AttributeKey<Json> = AttributeKey("json")

public var Application.json: Json
  get() = attributes[key]
  set(value) {
    attributes[key] = value
  }
