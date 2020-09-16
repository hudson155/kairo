package io.limberapp.backend.module.auth.api.org.role

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc

@Suppress("StringLiteralDuplication")
object OrgRoleApi {
  data class Post(val orgGuid: String, val rep: OrgRoleRep.Creation?) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/orgs/${enc(orgGuid)}/roles",
    body = rep
  )

  data class GetByOrgGuid(val orgGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/orgs/${enc(orgGuid)}/roles"
  )

  data class Patch(val orgGuid: String, val orgRoleGuid: String, val rep: OrgRoleRep.Update?) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}",
    body = rep
  )

  data class Delete(val orgGuid: String, val orgRoleGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}"
  )
}
