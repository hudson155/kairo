package io.limberapp.backend.module.auth.api.org.role

import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object OrgRoleApi {
  data class Post(val orgGuid: UUID, val rep: OrgRoleRep.Creation?) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/orgs/${enc(orgGuid)}/roles",
    body = rep
  )

  data class GetByOrgGuid(val orgGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/orgs/${enc(orgGuid)}/roles"
  )

  data class Patch(val orgGuid: UUID, val orgRoleGuid: UUID, val rep: OrgRoleRep.Update?) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}",
    body = rep
  )

  data class Delete(val orgGuid: UUID, val orgRoleGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}"
  )
}
