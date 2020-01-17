package com.piperframework.config

data class ConfigString(
    private val type: Type,
    private val value: String
) {

    enum class Type { PLAINTEXT, ENVIRONMENT_VARIABLE }
}
