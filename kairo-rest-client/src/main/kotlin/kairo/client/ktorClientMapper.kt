package kairo.client

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kairo.serialization.module.primitives.TrimWhitespace

internal val ktorClientMapper: JsonMapper =
  ObjectMapperFactory.builder(ObjectMapperFormat.Json) {
    prettyPrint = true
    trimWhitespace = TrimWhitespace.Type.TrimBoth
  }.build()
