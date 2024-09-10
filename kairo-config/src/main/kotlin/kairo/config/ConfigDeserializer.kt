package kairo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.commandRunner.CommandRunner
import kairo.protectedString.ProtectedString
import kairo.serialization.readValue
import kotlin.reflect.KClass

private val logger: KLogger = KotlinLogging.logger {}

@OptIn(ProtectedString.Access::class)
internal abstract class ConfigDeserializer<T : Any>(
  kClass: KClass<T>,
  private val config: ConfigLoaderConfig,
) : StdDeserializer<T>(kClass.java) {
  private val allowInsecureConfigSources: Boolean =
    config.environmentVariableSupplier.get("KAIRO_ALLOW_INSECURE_CONFIG_SOURCES") == true.toString()

  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T? {
    val source = p.readValue<ConfigLoaderSource>()
    if (!isSecure(source)) {
      if (!allowInsecureConfigSources) {
        throw IllegalArgumentException("Config loader source ${source::class.simpleName!!} is considered insecure.")
      }
      logger.warn { "Config loader source ${source::class.simpleName!!} is considered insecure." }
    }
    val string = when (source) {
      is ConfigLoaderSource.Command -> fromCommand(source)
      is ConfigLoaderSource.EnvironmentVariable -> fromEnvironmentVariable(source)
      is ConfigLoaderSource.GcpSecret -> fromGcpSecret(source)
    }
    return string?.let { convert(it) }
  }

  protected abstract fun isSecure(source: ConfigLoaderSource): Boolean

  private fun fromCommand(source: ConfigLoaderSource.Command): ProtectedString? {
    val (command) = source
    logger.debug { "Config value is from command: $command." }
    @OptIn(CommandRunner.Insecure::class)
    return config.commandRunner.run(command)?.let { ProtectedString(it) }
  }

  private fun fromEnvironmentVariable(source: ConfigLoaderSource.EnvironmentVariable): ProtectedString? {
    val (name, default) = source
    logger.debug { "Config value is from (INSECURE) environment variable: $name." }
    return config.environmentVariableSupplier.get(name, default)?.let { ProtectedString(it) }
  }

  private fun fromGcpSecret(source: ConfigLoaderSource.GcpSecret): ProtectedString? {
    val (id) = source
    logger.debug { "Config value is from GCP secret: $id." }
    return config.gcpSecretSupplier.get(id)
  }

  protected abstract fun convert(string: ProtectedString): T
}
