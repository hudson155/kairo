package io.limberapp.backend

import com.piperframework.config.Config
import com.piperframework.config.authentication.AuthenticationConfig
import com.piperframework.config.database.SqlDatabaseConfig
import com.piperframework.config.serving.ServingConfig

/**
 * The Config class contains all custom configuration for the app. It doesn't contain Ktor built-in configuration.
 */
data class Config(
    val sqlDatabase: SqlDatabaseConfig,
    override val authentication: AuthenticationConfig,
    override val serving: ServingConfig
) : Config
