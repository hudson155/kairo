package kairo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.commandRunner.CommandRunner
import kairo.protectedString.ProtectedString
import kairo.serialization.readValue

private val logger: KLogger = KotlinLogging.logger {}

internal class ConfigLoaderProtectedStringDeserializer(
  private val config: ConfigLoaderConfig,
) : StdDeserializer<ProtectedString>(ProtectedString::class.java) {
  private val allowInsecureConfigSources: Boolean =
    config.environmentVariableSupplier.get("KAIRO_ALLOW_INSECURE_CONFIG_SOURCES") == true.toString()

  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ProtectedString? =
    when (val token = p.currentToken) {
      JsonToken.VALUE_STRING -> fromString(p.readValue())
      JsonToken.START_OBJECT -> fromSource(p.readValue())
      else -> throw IllegalArgumentException("Unsupported token: $token.")
    }

  private fun fromString(string: String): ProtectedString {
    logger.debug { "Config value is from (INSECURE) string." }
    if (!allowInsecureConfigSources) {
      throw IllegalArgumentException("Protected strings cannot appear directly in configs.")
    }
    return ProtectedString(string)
  }

  private fun fromSource(source: ConfigLoaderProtectedStringSource): ProtectedString? =
    when (source) {
      is ConfigLoaderProtectedStringSource.Command -> fromCommand(source)
      is ConfigLoaderProtectedStringSource.EnvironmentVariable -> fromEnvironmentVariable(source)
      is ConfigLoaderProtectedStringSource.GcpSecret -> fromGcpSecret(source)
    }

  private fun fromCommand(source: ConfigLoaderProtectedStringSource.Command): ProtectedString? {
    val (command) = source
    logger.debug { "Config value is from (INSECURE) command: $command." }
    if (!allowInsecureConfigSources) {
      throw IllegalArgumentException("Command source is considered insecure.")
    }
    @OptIn(CommandRunner.Insecure::class)
    return config.commandRunner.run(command)?.let { ProtectedString(it) }
  }

  private fun fromEnvironmentVariable(source: ConfigLoaderProtectedStringSource.EnvironmentVariable): ProtectedString? {
    val (name, default) = source
    logger.debug { "Config value is from (INSECURE) environment variable: $name." }
    if (!allowInsecureConfigSources) {
      throw IllegalArgumentException("Environment variable source is considered insecure.")
    }
    return config.environmentVariableSupplier.get(name, default)?.let { ProtectedString(it) }
  }

  private fun fromGcpSecret(source: ConfigLoaderProtectedStringSource.GcpSecret): ProtectedString? {
    val (id) = source
    logger.debug { "Config value is from GCP secret: $id." }
    // Secure.
    return config.gcpSecretSupplier.get(id)
  }
}
