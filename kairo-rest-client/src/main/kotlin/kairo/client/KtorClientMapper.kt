package kairo.client

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.jsonMapper
import kairo.serialization.property.allowUnknownProperties
import kairo.serialization.property.prettyPrint

public object KtorClientMapper {
  public val json: JsonMapper =
    jsonMapper { configure() }.build()

  private fun ObjectMapperFactory<*, *>.configure() {
    allowUnknownProperties = true
    prettyPrint = true
  }
}
