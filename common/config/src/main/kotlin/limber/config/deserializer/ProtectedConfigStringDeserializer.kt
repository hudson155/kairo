package limber.config.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import limber.config.ConfigString
import limber.type.ProtectedString
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}

public class ProtectedConfigStringDeserializer : StdDeserializer<ProtectedString>(ProtectedString::class.java) {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ProtectedString? {
    logger.info { "Deserializing config string..." }
    val configString = p.readValueAs(ConfigString::class.java)
    return when (configString.type) {
      ConfigString.Type.Plaintext -> ConfigStringDeserializer.fromPlaintext(configString)
        ?.let { ProtectedString(it) }
      ConfigString.Type.EnvironmentVariable -> ConfigStringDeserializer.fromEnvironmentVariable(configString)
        ?.let { ProtectedString(it) }
      ConfigString.Type.GcpSecret -> fromGcpSecret(configString)
      ConfigString.Type.Command -> fromCommand(configString)
    }
  }

  public companion object {
    private fun fromGcpSecret(configString: ConfigString): ProtectedString? {
      requireNotNull(configString.value)
      logger.info {
        "Config string is from GCP secret." +
          " Accessing environment variable with name ${configString.value}."
      }
      val secretId = EnvironmentVariableSource[configString.value]
        ?: error("Environment variable was not set.")
      logger.info { "Accessing GCP secret with ID $secretId." }
      val value = GcpSecretSource[secretId]
      if (value != null) {
        logger.info { "Retrieved config string from GCP secret. Not logging due to sensitivity." }
        return ProtectedString(value)
      } else {
        logger.info { "GCP secret was not set. Using null." }
        return null
      }
    }

    private fun fromCommand(configString: ConfigString): ProtectedString? {
      requireNotNull(configString.value)
      logger.info { "Config string is from command. Running command \"${configString.value}\"." }
      val value = CommandSource[configString.value]
      logger.info { "Retrieved config string from command. Not logging due to possible sensitivity." }
      return ProtectedString(value)
    }
  }
}
