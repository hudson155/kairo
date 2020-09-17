package io.limberapp.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.slf4j.LoggerFactory

class ConfigStringDeserializer : StdDeserializer<String>(String::class.java) {
  private val logger = LoggerFactory.getLogger(ConfigStringDeserializer::class.java)

  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String? {
    logger.info("Deserializing config string.")
    val configString = p.readValueAs(ConfigString::class.java)
    logger.info("Config string value to deserialize: $configString.")
    return when (configString) {
      is ConfigString.Plaintext -> configString.value
      is ConfigString.EnvironmentVariable -> System.getenv(configString.name) ?: configString.defaultValue
    }.also {
      logger.info("Config string deserialization complete. Not logging value due to possible sensitivity.")
    }
  }
}
