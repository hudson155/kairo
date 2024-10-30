package kairo.rest

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.jsonMapper
import kairo.serialization.module.primitives.TrimWhitespace
import kairo.serialization.module.primitives.trimWhitespace
import kairo.serialization.property.prettyPrint

public val ktorServerMapper: JsonMapper =
  jsonMapper {
    prettyPrint = true
    trimWhitespace = TrimWhitespace.Type.TrimBoth
  }.build()
