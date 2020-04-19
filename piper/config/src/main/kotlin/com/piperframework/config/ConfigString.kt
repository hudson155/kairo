package com.piperframework.config

/**
 * Allows for use of environment variables rather than explicit strings.
 */
data class ConfigString(
    val type: Type,
    val value: String
) {

    enum class Type { PLAINTEXT, ENVIRONMENT_VARIABLE }
}
