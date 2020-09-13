package com.piperframework.config

import com.piperframework.config.authentication.AuthenticationConfig
import com.piperframework.config.hashing.HashingConfig

/**
 * The Config interface contains required configuration for the app. It doesn't contain Ktor built-in configuration.
 */
interface Config {
  val authentication: AuthenticationConfig
  val hashing: HashingConfig
}
