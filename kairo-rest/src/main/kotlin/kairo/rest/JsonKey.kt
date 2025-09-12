package kairo.rest

import io.ktor.util.AttributeKey
import io.ktor.util.Attributes
import kotlinx.serialization.json.Json

private val key: AttributeKey<Json> = AttributeKey<Json>("Json")

public var Attributes.json: Json
  get() = get(key)
  set(value) {
    put(key, value)
  }
