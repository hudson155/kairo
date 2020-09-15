package io.limberapp.common.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class ConfigStringDeserializer : JsonDeserializer<String?>() {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String? {
    return when (val configString = p.readValueAs(ConfigString::class.java)) {
      is ConfigString.Plaintext -> configString.value
      is ConfigString.EnvironmentVariable -> System.getenv(configString.name) ?: configString.defaultValue
    }
  }
}
