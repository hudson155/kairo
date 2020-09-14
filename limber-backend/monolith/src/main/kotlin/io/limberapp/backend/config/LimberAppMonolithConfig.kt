package io.limberapp.backend.config

import io.limberapp.common.config.Config
import io.limberapp.common.config.ConfigString
import io.limberapp.common.config.authentication.AuthenticationConfig
import io.limberapp.common.config.authentication.AuthenticationMechanism
import io.limberapp.common.config.database.SqlDatabaseConfig
import io.limberapp.common.config.hashing.HashingConfig

/**
 * The config class contains all custom configuration for the app. It doesn't contain Ktor built-in configuration.
 */
data class LimberAppMonolithConfig(
  val sqlDatabase: SqlDatabaseConfig,
  override val authentication: AuthenticationConfig,
  override val hashing: HashingConfig,
) : Config {
  fun decrypt() = copy(sqlDatabase = sqlDatabase.decrypt(), authentication = authentication.decrypt())

  private fun SqlDatabaseConfig.decrypt() = copy(
    jdbcUrl = jdbcUrl.decryptNotNull(),
    username = username.decryptNotNull(),
    password = password.decryptNullable(),
  )

  private fun AuthenticationConfig.decrypt() = copy(mechanisms = mechanisms.map { it.decrypt() })

  private fun AuthenticationMechanism.decrypt() =
    if (this is AuthenticationMechanism.Jwt) copy(secret = secret.decryptNotNull()) else this

  private fun ConfigString.decryptNotNull() = requireNotNull(decryptNullable())

  private fun ConfigString?.decryptNullable(): ConfigString.Plaintext? = when (this) {
    null -> null
    is ConfigString.Plaintext -> this
    is ConfigString.EnvironmentVariable -> System.getenv(name).let { value: String? ->
      (value ?: defaultValue)?.let { ConfigString.Plaintext(value = it) }
    }
  }
}
