package kairo.sql

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.jsonMapper
import kairo.serialization.property.allowUnknownProperties

internal val sqlMapper: JsonMapper =
  jsonMapper {
    allowUnknownProperties = true
  }.build()