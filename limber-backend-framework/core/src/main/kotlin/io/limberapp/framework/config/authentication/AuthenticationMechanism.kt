package io.limberapp.framework.config.authentication

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = JwkAuthentication::class, name = "JWK"),
    JsonSubTypes.Type(value = JwtAuthentication::class, name = "JWT"),
    JsonSubTypes.Type(value = UnsignedJwtAuthentication::class, name = "UNSIGNED_JWT")
)
sealed class AuthenticationMechanism

data class JwkAuthentication(val domain: String) : AuthenticationMechanism()

data class JwtAuthentication(val secret: String) : AuthenticationMechanism()

object UnsignedJwtAuthentication : AuthenticationMechanism()
