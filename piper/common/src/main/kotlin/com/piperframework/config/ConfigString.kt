package com.piperframework.config

data class ConfigString(
    val type: Type,
    val value: String
) {

    enum class Type { PLAINTEXT, ENVIRONMENT_VARIABLE }
}
