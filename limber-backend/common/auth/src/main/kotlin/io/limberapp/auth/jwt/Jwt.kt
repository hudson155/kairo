package io.limberapp.auth.jwt

import com.auth0.jwt.JWTCreator
import io.limberapp.common.serialization.limberObjectMapper
import io.limberapp.permissions.AccountRole

data class Jwt(
    val org: JwtOrg?,
    val roles: Set<AccountRole>,
    val user: JwtUser?,
) {
  companion object {
    fun withOnlyRole(accountRole: AccountRole) = Jwt(null, setOf(accountRole), null)
  }
}

private val objectMapper = limberObjectMapper()

fun JWTCreator.Builder.withJwt(jwt: Jwt): JWTCreator.Builder {
  withClaim(JwtClaims.org, jwt.org?.let { objectMapper.writeValueAsString(it) })
  withClaim(JwtClaims.roles, objectMapper.writeValueAsString(jwt.roles))
  withClaim(JwtClaims.user, jwt.user?.let { objectMapper.writeValueAsString(it) })
  return this
}
