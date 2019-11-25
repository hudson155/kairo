package com.piperframework.config.authentication

data class AuthenticationConfig(
    val mechanisms: List<com.piperframework.config.authentication.AuthenticationMechanism>
)
