package limber.config.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import limber.config.ConfigString
import limber.type.ProtectedString
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

public class ProtectedConfigStringDeserializer : StdDeserializer<ProtectedString>(ProtectedString::class.java) {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ProtectedString? {
    logger.info { "Deserializing config string..." }
    return when (val configString = p.readValueAs(ConfigString::class.java)) {
      is ConfigString.Plaintext -> ConfigStringDeserializer.fromPlaintext(configString)
        ?.let { ProtectedString(it) }
      is ConfigString.EnvironmentVariable -> ConfigStringDeserializer.fromEnvironmentVariable(configString)
        ?.let { ProtectedString(it) }
      is ConfigString.GcpSecret -> fromGcpSecret(configString)
      is ConfigString.Command -> fromCommand(configString)
    }
  }

  public companion object {
    private fun fromGcpSecret(configString: ConfigString.GcpSecret): ProtectedString? {
      logger.info {
        "Config string is from GCP secret." +
          " Accessing environment variable with name ${configString.environmentVariableName}."
      }
      val secretId = EnvironmentVariableSource[configString.environmentVariableName]
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

    private fun fromCommand(configString: ConfigString.Command): ProtectedString? {
      logger.info { "Config string is from command. Running command \"${configString.command}\"." }
      val value = CommandSource[configString.command]
      logger.info { "Retrieved config string from command. Not logging due to possible sensitivity." }
      return ProtectedString(value)
    }
  }
}
