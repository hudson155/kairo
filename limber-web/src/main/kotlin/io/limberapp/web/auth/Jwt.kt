package io.limberapp.web.auth

import io.limberapp.backend.authorization.principal.Claims
import io.limberapp.backend.authorization.principal.JwtOrg
import io.limberapp.backend.authorization.principal.JwtUser
import io.limberapp.web.api.json
import io.limberapp.web.util.external.decodeJwt

internal data class Jwt(val raw: String) {
  private val decoded = decodeJwt(raw)

  val org = json.parse<JwtOrg>(JSON.stringify(decoded[Claims.org]))

  val user = json.parse<JwtUser>(JSON.stringify(decoded[Claims.user]))
}
