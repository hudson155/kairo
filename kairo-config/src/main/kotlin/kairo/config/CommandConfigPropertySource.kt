package kairo.config

import kairo.commandRunner.CommandRunner
import kairo.commandRunner.DefaultCommandRunner
import kairo.config.CommandConfigPropertySource.ConfigProperty
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class CommandConfigPropertySource(
  private val commandRunner: CommandRunner = DefaultCommandRunner(),
) : ConfigPropertySource<ConfigProperty>() {
  @Serializable
  @SerialName("Command")
  public data class ConfigProperty(
    val command: String,
  ) : ConfigPropertySource.ConfigProperty()

  override val serializer: KSerializer<ConfigProperty> = ConfigProperty.serializer()

  @OptIn(CommandRunner.Insecure::class)
  override suspend fun resolve(property: ConfigProperty): String =
    commandRunner.run(property.command).readText()
}
