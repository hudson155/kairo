package com.piperframework.config.hashing

/**
 * Configures password and token hashing.
 */
data class HashingConfig(
  val logRounds: Int,
)
