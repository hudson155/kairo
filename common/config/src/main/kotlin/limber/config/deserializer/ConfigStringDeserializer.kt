package limber.config.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import limber.config.ConfigString
import mu.KotlinLogging
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

public class ConfigStringDeserializer : StdDeserializer<String>(String::class.java) {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String? {
    logger.info { "Deserializing config string..." }
    return when (val configString = p.readValueAs(ConfigString::class.java)) {
      is ConfigString.Plaintext -> fromPlaintext(configString)
      is ConfigString.EnvironmentVariable -> fromEnvironmentVariable(configString)
      is ConfigString.GcpSecret, is ConfigString.Command -> mustBeProtected(configString::class)
    }
  }

  public companion object {
    internal fun fromPlaintext(configString: ConfigString.Plaintext): String? {
      logger.info { "Config string is from plaintext." }
      val value = configString.value
      if (value != null) {
        logger.info { "Config string value is $value." }
        return value
      }
      logger.info { "Config string value was not provided. Using null." }
      return null
    }

    internal fun fromEnvironmentVariable(configString: ConfigString.EnvironmentVariable): String? {
      logger.info {
        "Config string is from environment variable." +
          " Accessing environment variable with name ${configString.name}."
      }
      val value = EnvironmentVariableSource.get(configString.name)
      if (value != null) {
        logger.info { "Retrieved config string value from environment variable. Value is $value." }
        return value
      }
      logger.info { "Environment variable was not set. Using null." }
      return null
    }

    private fun mustBeProtected(kClass: KClass<out ConfigString>): Nothing =
      error("Config strings with source ${kClass.simpleName} must be protected.")
  }
}
