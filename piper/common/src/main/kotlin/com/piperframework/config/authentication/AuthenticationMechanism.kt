package com.piperframework.config.authentication

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = com.piperframework.config.authentication.JwkAuthentication::class, name = "JWK"),
    JsonSubTypes.Type(value = com.piperframework.config.authentication.JwtAuthentication::class, name = "JWT"),
    JsonSubTypes.Type(value = com.piperframework.config.authentication.UnsignedJwtAuthentication::class, name = "UNSIGNED_JWT")
)
sealed class AuthenticationMechanism {
    abstract val issuer: String?
}

data class JwkAuthentication(
    override val issuer: String,
    val domain: String
) : com.piperframework.config.authentication.AuthenticationMechanism()

data class JwtAuthentication(
    override val issuer: String,
    val secret: String
) : com.piperframework.config.authentication.AuthenticationMechanism()

object UnsignedJwtAuthentication : com.piperframework.config.authentication.AuthenticationMechanism() {
    override val issuer: Nothing? = null
}
