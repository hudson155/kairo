package kairo.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "source")
@JsonSubTypes(
  JsonSubTypes.Type(ConfigLoaderStringSource.Command::class, name = "Command"),
  JsonSubTypes.Type(ConfigLoaderStringSource.EnvironmentVariable::class, name = "EnvironmentVariable"),
)
internal sealed class ConfigLoaderStringSource {
  internal data class Command(
    val command: String,
  ) : ConfigLoaderStringSource()

  internal data class EnvironmentVariable(
    val name: String,
    val default: String?,
  ) : ConfigLoaderStringSource()
}
