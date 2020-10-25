package io.limberapp.common.auth.jwt

import com.auth0.jwt.JWTCreator
import com.fasterxml.jackson.module.kotlin.convertValue
import io.limberapp.common.serialization.limberObjectMapper

private val objectMapper = limberObjectMapper()

fun JWTCreator.Builder.withJwt(jwt: Jwt): JWTCreator.Builder {
  withClaim(JwtClaims.org, jwt.org?.let { objectMapper.convertValue<Map<String, Any>>(it) })
  withClaim(JwtClaims.roles, objectMapper.convertValue<List<String>>(jwt.roles))
  withClaim(JwtClaims.user, jwt.user?.let { objectMapper.convertValue<Map<String, Any>>(it) })
  return this
}
