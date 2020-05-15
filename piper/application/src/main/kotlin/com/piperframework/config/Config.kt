package com.piperframework.config

import com.piperframework.config.authentication.AuthenticationConfig
import com.piperframework.config.hashing.HashingConfig
import com.piperframework.config.serving.ServingConfig

/**
 * The Config interface contains required configuration for the app. It doesn't contain Ktor built-in configuration.
 */
interface Config {
  val authentication: AuthenticationConfig
  val hashing: HashingConfig
  val serving: ServingConfig
}
