package io.limberapp.common.config.authentication

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.limberapp.common.config.ConfigString

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = AuthenticationMechanism.Jwk::class, name = "JWK"),
  JsonSubTypes.Type(value = AuthenticationMechanism.Jwt::class, name = "JWT"),
  JsonSubTypes.Type(value = AuthenticationMechanism.UnsignedJwt::class, name = "UNSIGNED_JWT")
)
sealed class AuthenticationMechanism {
  abstract val issuer: String?
  abstract val leeway: Long

  data class Jwk(
    override val issuer: String,
    override val leeway: Long,
    val domain: String,
  ) : AuthenticationMechanism()

  data class Jwt(
    override val issuer: String,
    override val leeway: Long,
    val secret: ConfigString,
  ) : AuthenticationMechanism()

  data class UnsignedJwt(
    override val leeway: Long,
  ) : AuthenticationMechanism() {
    override val issuer: Nothing? = null
  }
}
