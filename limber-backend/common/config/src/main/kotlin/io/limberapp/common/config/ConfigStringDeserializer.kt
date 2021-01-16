package io.limberapp.common.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RequiresOptIn
internal annotation class EnvironmentManipulation

/**
 * The [getEnv] getter defines how environment variables should be fetched for this entire library.
 * [System.getenv] should always be used in production code. Using this as a delegate function,
 * however, allows for proper testing. In order to avoid changing how environment variables are
 * fetched in production, the [EnvironmentManipulation] annotation prevents modification without
 * explicit opt-in, which should only be in test code.
 */
internal var getEnv: (name: String) -> String? = System::getenv
  @EnvironmentManipulation set

class ConfigStringDeserializer : StdDeserializer<String>(String::class.java) {
  private val logger: Logger = LoggerFactory.getLogger(ConfigStringDeserializer::class.java)

  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String? {
    logger.info("Deserializing config string...")
    val configString = p.readValueAs(ConfigString::class.java)
    return when (configString) {
      is ConfigString.Plaintext -> run {
        logger.info("Config string value is plaintext: ${configString.value}.")
        return@run configString.value
      }
      is ConfigString.EnvironmentVariable -> run {
        logger.info("Config string value is from environment." +
            " Accessing environment variable: ${configString.name}.")
        val value = getEnv(configString.name)
        value?.let {
          logger.info("Retrieved value from environment variable ${configString.name}." +
              " Not logging value due to possible sensitivity.")
          return@run it
        }
        configString.defaultValue?.let {
          logger.info("Environment variable ${configString.name} was not set." +
              " Using default value of ${configString.defaultValue}.")
          return@run it
        }
        logger.info("Environment variable ${configString.name} was not set." +
            " No default value - using null.")
        return@run null
      }
    }
  }
}
