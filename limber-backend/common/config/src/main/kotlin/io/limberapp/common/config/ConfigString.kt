package io.limberapp.common.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Allows for use of environment variables rather than explicit strings.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = ConfigString.Plaintext::class, name = "PLAINTEXT"),
  JsonSubTypes.Type(value = ConfigString.EnvironmentVariable::class, name = "ENVIRONMENT_VARIABLE"),
)
sealed class ConfigString {
  abstract val value: String

  data class Plaintext(override val value: String) : ConfigString()

  data class EnvironmentVariable(val name: String, val defaultValue: String? = null) : ConfigString() {
    override val value get() = error("Not decrypted.")
  }
}
