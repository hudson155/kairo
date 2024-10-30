package kairo.client

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.jsonMapper
import kairo.serialization.property.allowUnknownProperties
import kairo.serialization.property.prettyPrint

public val ktorClientMapper: JsonMapper =
  jsonMapper {
    allowUnknownProperties = true
    prettyPrint = true
  }.build()
