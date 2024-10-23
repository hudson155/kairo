package kairo.client

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.jsonMapper

public val ktorClientMapper: JsonMapper =
  jsonMapper {
    allowUnknownProperties = true
    prettyPrint = true
  }
