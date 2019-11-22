package io.limberapp.framework.config.authentication

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = JwkAuthentication::class, name = "JWK"),
    JsonSubTypes.Type(value = JwtAuthentication::class, name = "JWT"),
    JsonSubTypes.Type(value = UnsignedJwtAuthentication::class, name = "UNSIGNED_JWT")
)
sealed class AuthenticationMechanism {
    abstract val issuer: String?
}

data class JwkAuthentication(
    override val issuer: String,
    val domain: String
) : AuthenticationMechanism()

data class JwtAuthentication(
    override val issuer: String,
    val secret: String
) : AuthenticationMechanism()

object UnsignedJwtAuthentication : AuthenticationMechanism() {
    override val issuer: Nothing? = null
}
