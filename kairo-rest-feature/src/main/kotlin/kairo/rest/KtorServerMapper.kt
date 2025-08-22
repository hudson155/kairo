package kairo.rest

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.jsonMapper
import kairo.serialization.module.primitives.TrimWhitespace
import kairo.serialization.module.primitives.trimWhitespace
import kairo.serialization.property.prettyPrint

public object KtorServerMapper {
  public val json: JsonMapper =
    jsonMapper { configure() }.build()

  private fun ObjectMapperFactory<*, *>.configure() {
    prettyPrint = true
    trimWhitespace = TrimWhitespace.Type.TrimBoth
  }
}
