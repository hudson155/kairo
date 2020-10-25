package io.limberapp.common.auth.jwt

import com.fasterxml.jackson.annotation.JsonProperty
import io.limberapp.common.permissions.AccountRole

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
