package io.limberapp.backend.config

import com.piperframework.config.Config
import com.piperframework.config.ConfigString
import com.piperframework.config.authentication.AuthenticationConfig
import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.config.serving.ServingConfig

/**
 * The config class contains all custom configuration for the app. It doesn't contain Ktor built-in configuration.
 */
internal data class LimberAppMonolithConfig(
    val sqlDatabase: SqlDatabaseConfig,
    override val authentication: AuthenticationConfig,
    override val serving: ServingConfig
) : Config {

    fun decrypt() = copy(sqlDatabase = sqlDatabase.decrypt())

    private fun SqlDatabaseConfig.decrypt() = copy(jdbcUrl = jdbcUrl.decrypt(), password = password?.decrypt())

    private fun ConfigString.decrypt(): ConfigString {
        return when (type) {
            ConfigString.Type.PLAINTEXT -> this
            ConfigString.Type.ENVIRONMENT_VARIABLE -> copy(value = checkNotNull(System.getenv(this.value)))
        }
    }
}
