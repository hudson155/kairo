package kairo.rest

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.jsonMapper
import kairo.serialization.module.primitives.TrimWhitespace
import kairo.serialization.xmlMapper

public object KtorServerMapper {
  public val json: JsonMapper =
    jsonMapper {
      configureKtorServerMapper()
    }

  public val xml: XmlMapper =
    xmlMapper {
      configureKtorServerMapper()
    }

  private fun ObjectMapperFactory<*, *>.configureKtorServerMapper() {
    prettyPrint = true
    trimWhitespace = TrimWhitespace.Type.TrimBoth
  }
}
