package kairo.config

import kairo.commandRunner.CommandRunner
import kairo.commandRunner.DefaultCommandRunner
import kairo.config.CommandConfigPropertySource.Property
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class CommandConfigPropertySource(
  private val commandRunner: CommandRunner = DefaultCommandRunner(),
) : ConfigPropertySource<Property>() {
  @Serializable
  @SerialName("Command")
  public data class Property(
    val command: String,
  ) : ConfigProperty()

  override val serializer: KSerializer<Property> = Property.serializer()

  @OptIn(CommandRunner.Insecure::class)
  override suspend fun resolve(property: Property): String =
    commandRunner.run(property.command).readText()
}
