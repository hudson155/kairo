package kairo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.commandRunner.CommandRunner
import kairo.serialization.StringDeserializer
import kairo.serialization.readValue

private val logger: KLogger = KotlinLogging.logger {}

internal class ConfigLoaderStringDeserializer(
  private val config: ConfigLoaderConfig,
) : StringDeserializer() {
  private val allowInsecureConfigSources: Boolean =
    config.environmentVariableSupplier.get("KAIRO_ALLOW_INSECURE_CONFIG_SOURCES") == true.toString()

  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String? =
    when (val token = p.currentToken) {
      JsonToken.VALUE_STRING -> super.deserialize(p, ctxt)
      JsonToken.START_OBJECT -> fromSource(p.readValue())
      else -> throw IllegalArgumentException("Unsupported token: $token.")
    }

  private fun fromSource(source: ConfigLoaderStringSource): String? =
    when (source) {
      is ConfigLoaderStringSource.Command -> fromCommand(source)
      is ConfigLoaderStringSource.EnvironmentVariable -> fromEnvironmentVariable(source)
    }

  private fun fromCommand(source: ConfigLoaderStringSource.Command): String? {
    val (command) = source
    logger.info { "Config string is from (INSECURE) command: $command." }
    if (!allowInsecureConfigSources) {
      throw IllegalArgumentException("Command source is considered insecure.")
    }
    @OptIn(CommandRunner.Insecure::class)
    return config.commandRunner.run(command)
  }

  private fun fromEnvironmentVariable(source: ConfigLoaderStringSource.EnvironmentVariable): String? {
    val (name, default) = source
    logger.info { "Config string is from environment variable: $name." }
    return config.environmentVariableSupplier.get(name, default)
  }
}
