package io.limberapp.backend

import com.piperframework.config.Config
import com.piperframework.config.authentication.AuthenticationConfig
import com.piperframework.config.database.MongoDatabaseConfig
import com.piperframework.config.serving.ServingConfig

/**
 * The Config class contains all custom configuration for the app. It doesn't contain Ktor built-in configuration.
 */
data class Config(
    val mongoDatabase: MongoDatabaseConfig,
    override val authentication: com.piperframework.config.authentication.AuthenticationConfig,
    override val serving: ServingConfig
) : com.piperframework.config.Config
