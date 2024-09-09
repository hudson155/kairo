package kairo.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "source")
@JsonSubTypes(
  JsonSubTypes.Type(ConfigLoaderProtectedStringSource.Command::class, name = "Command"),
  JsonSubTypes.Type(ConfigLoaderProtectedStringSource.EnvironmentVariable::class, name = "EnvironmentVariable"),
  JsonSubTypes.Type(ConfigLoaderProtectedStringSource.GcpSecret::class, name = "GcpSecret"),
)
internal sealed class ConfigLoaderProtectedStringSource {
  internal data class Command(
    val command: String,
  ) : ConfigLoaderProtectedStringSource()

  internal data class EnvironmentVariable(
    val name: String,
    val default: String?,
  ) : ConfigLoaderProtectedStringSource()

  internal data class GcpSecret(
    val id: String,
  ) : ConfigLoaderProtectedStringSource()
}
