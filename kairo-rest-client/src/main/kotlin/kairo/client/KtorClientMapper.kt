package kairo.client

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.jsonMapper
import kairo.serialization.property.allowUnknownProperties
import kairo.serialization.property.prettyPrint
import kairo.serialization.xmlMapper

public object KtorClientMapper {
  public val json: JsonMapper =
    jsonMapper {
      configureKtorClientMapper()
    }

  public val xml: XmlMapper =
    xmlMapper {
      configureKtorClientMapper()
    }

  private fun ObjectMapperFactory<*, *>.configureKtorClientMapper() {
    allowUnknownProperties = true
    prettyPrint = true
  }
}
