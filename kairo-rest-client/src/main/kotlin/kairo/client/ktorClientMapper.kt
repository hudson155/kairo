package kairo.client

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat

internal val ktorClientMapper: JsonMapper =
  ObjectMapperFactory.builder(ObjectMapperFormat.Json) {
    allowUnknownProperties = true
    prettyPrint = true
  }.build()
