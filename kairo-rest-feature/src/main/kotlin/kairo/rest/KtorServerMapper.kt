package kairo.rest

import com.fasterxml.jackson.databind.json.JsonMapper
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.jsonMapper
import kairo.serialization.module.primitives.TrimWhitespace

public object KtorServerMapper {
  public val json: JsonMapper =
    jsonMapper {
      configureKtorServerMapper()
    }

  private fun ObjectMapperFactory<*, *>.configureKtorServerMapper() {
    prettyPrint = true
    trimWhitespace = TrimWhitespace.Type.TrimBoth
  }
}
