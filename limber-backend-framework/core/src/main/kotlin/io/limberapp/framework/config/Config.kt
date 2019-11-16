package io.limberapp.framework.config

import io.limberapp.framework.config.jwt.JwtConfig
import io.limberapp.framework.config.serving.ServingConfig

/**
 * The Config interface contains required configuration for the app. It doesn't contain Ktor
 * built-in configuration.
 */
interface Config {
    val jwt: JwtConfig
    val serving: ServingConfig
}
