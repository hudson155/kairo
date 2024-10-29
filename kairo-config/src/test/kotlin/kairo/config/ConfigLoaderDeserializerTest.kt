package kairo.config

import com.fasterxml.jackson.databind.json.JsonMapper
import io.mockk.every
import io.mockk.mockk
import kairo.commandRunner.CommandRunner
import kairo.commandRunner.DefaultCommandRunner
import kairo.environmentVariableSupplier.EnvironmentVariableSupplier
import kairo.gcpSecretSupplier.GcpSecretSupplier
import kairo.serialization.jsonMapper

/**
 * There are a lot of test cases for config loader deserialization,
 * so they're broken up into subclasses of this class.
 */
internal abstract class ConfigLoaderDeserializerTest {
  protected val commandRunner: CommandRunner = DefaultCommandRunner
  protected val environmentVariableSupplier: EnvironmentVariableSupplier = mockk()
  protected val gcpSecretSupplier: GcpSecretSupplier = mockk()

  protected fun allowInsecureConfigSources(boolean: Boolean) {
    every { environmentVariableSupplier["KAIRO_ALLOW_INSECURE_CONFIG_SOURCES", any()] } answers {
      return@answers if (boolean) true.toString() else secondArg()
    }
  }

  protected fun createMapper(): JsonMapper {
    val config = ConfigLoaderConfig(
      commandRunner = commandRunner,
      environmentVariableSupplier = environmentVariableSupplier,
      gcpSecretSupplier = gcpSecretSupplier,
    )
    return jsonMapper().build {
      addModule(ConfigLoaderModule.create(config))
    }
  }
}
