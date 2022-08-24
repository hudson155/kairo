package limber.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = ConfigString.Plaintext::class, name = "Plaintext"),
  JsonSubTypes.Type(value = ConfigString.EnvironmentVariable::class, name = "EnvironmentVariable"),
  JsonSubTypes.Type(value = ConfigString.GcpSecret::class, name = "GcpSecret"),
  JsonSubTypes.Type(value = ConfigString.Command::class, name = "Command"),
)
internal sealed class ConfigString {
  internal data class Plaintext(val value: String?) : ConfigString()
  internal data class EnvironmentVariable(val name: String) : ConfigString()
  internal data class GcpSecret(val environmentVariableName: String) : ConfigString()
  internal data class Command(val command: String) : ConfigString()
}
