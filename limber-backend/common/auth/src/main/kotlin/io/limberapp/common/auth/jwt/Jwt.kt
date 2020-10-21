package io.limberapp.common.auth.jwt

import com.auth0.jwt.JWTCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.convertValue
import io.limberapp.common.permissions.AccountRole
import io.limberapp.common.serialization.limberObjectMapper

data class Jwt(
    @JsonProperty(JwtClaims.org)
    val org: JwtOrg?,
    @JsonProperty(JwtClaims.roles)
    val roles: Set<AccountRole>,
    @JsonProperty(JwtClaims.user)
    val user: JwtUser?,
) {
  companion object {
    fun withOnlyRole(accountRole: AccountRole) = Jwt(null, setOf(accountRole), null)
  }
}

private val objectMapper = limberObjectMapper()

fun JWTCreator.Builder.withJwt(jwt: Jwt): JWTCreator.Builder {
  withClaim(JwtClaims.org, jwt.org?.let { objectMapper.convertValue<Map<String, Any>>(it) })
  withClaim(JwtClaims.roles, objectMapper.convertValue<List<String>>(jwt.roles))
  withClaim(JwtClaims.user, jwt.user?.let { objectMapper.convertValue<Map<String, Any>>(it) })
  return this
}
