package io.limberapp.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(
        value = ConfigString.Plaintext::class,
        name = "PLAINTEXT",
    ),
    JsonSubTypes.Type(
        value = ConfigString.EnvironmentVariable::class,
        name = "ENVIRONMENT_VARIABLE",
    ),
)
internal sealed class ConfigString {
  internal data class Plaintext(
      val value: String,
  ) : ConfigString()

  internal data class EnvironmentVariable(
      val name: String,
      val defaultValue: String? = null,
  ) : ConfigString()
}
