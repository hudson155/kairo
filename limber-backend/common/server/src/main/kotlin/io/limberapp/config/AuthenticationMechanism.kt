package io.limberapp.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.auth0.jwt.algorithms.Algorithm as JwtAlgorithm

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = AuthenticationMechanism.Jwk::class, name = "JWK"),
    JsonSubTypes.Type(value = AuthenticationMechanism.Jwt.Signed::class, name = "JWT"),
    JsonSubTypes.Type(value = AuthenticationMechanism.Jwt.Unsigned::class, name = "UNSIGNED_JWT"),
)
sealed class AuthenticationMechanism {
  abstract val issuer: String?
  abstract val leeway: Long

  data class Jwk(
      override val issuer: String,
      override val leeway: Long,
      val domain: String,
  ) : AuthenticationMechanism()

  sealed class Jwt : AuthenticationMechanism() {
    internal abstract fun createAlgorithm(): JwtAlgorithm

    data class Signed(
        override val issuer: String,
        override val leeway: Long,
        val algorithm: Algorithm,
        @JsonDeserialize(using = ConfigStringDeserializer::class)
        val secret: String,
    ) : Jwt() {
      enum class Algorithm { HMAC256 }

      override fun createAlgorithm(): JwtAlgorithm = when (algorithm) {
        Algorithm.HMAC256 -> JwtAlgorithm.HMAC256(secret)
      }
    }

    data class Unsigned(
        override val leeway: Long,
    ) : Jwt() {
      override val issuer: String? = null

      override fun createAlgorithm(): JwtAlgorithm = JwtAlgorithm.none()
    }
  }
}
