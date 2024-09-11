package kairo.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "source")
@JsonSubTypes(
  JsonSubTypes.Type(ConfigLoaderSource.Command::class, name = "Command"),
  JsonSubTypes.Type(ConfigLoaderSource.EnvironmentVariable::class, name = "EnvironmentVariable"),
  JsonSubTypes.Type(ConfigLoaderSource.GcpSecret::class, name = "GcpSecret"),
)
internal sealed class ConfigLoaderSource {
  internal data class Command(
    val command: String,
  ) : ConfigLoaderSource()

  internal data class EnvironmentVariable(
    val name: String,
    val default: String?,
  ) : ConfigLoaderSource()

  internal data class GcpSecret(
    val id: String,
  ) : ConfigLoaderSource()
}
