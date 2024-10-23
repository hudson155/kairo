package kairo.rest

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.jsonMapper
import kairo.serialization.module.primitives.TrimWhitespace

public val ktorServerMapper: JsonMapper =
  jsonMapper {
    prettyPrint = true
    trimWhitespace = TrimWhitespace.Type.TrimBoth
  }
