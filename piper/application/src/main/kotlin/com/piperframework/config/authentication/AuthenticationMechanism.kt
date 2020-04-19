package com.piperframework.config.authentication

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = AuthenticationMechanism.Jwk::class, name = "JWK"),
    JsonSubTypes.Type(value = AuthenticationMechanism.Jwt::class, name = "JWT"),
    JsonSubTypes.Type(value = AuthenticationMechanism.UnsignedJwt::class, name = "UNSIGNED_JWT")
)
sealed class AuthenticationMechanism {

    abstract val issuer: String?

    data class Jwk(
        override val issuer: String,
        val domain: String
    ) : AuthenticationMechanism()

    data class Jwt(
        override val issuer: String,
        val secret: String
    ) : AuthenticationMechanism()

    object UnsignedJwt : AuthenticationMechanism() {
        override val issuer: Nothing? = null
    }
}
