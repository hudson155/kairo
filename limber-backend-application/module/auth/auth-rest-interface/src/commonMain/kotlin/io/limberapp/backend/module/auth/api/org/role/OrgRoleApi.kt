package io.limberapp.backend.module.auth.api.org.role

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep

@Suppress("StringLiteralDuplication")
object OrgRoleApi {
  data class Post(val orgGuid: UUID, val rep: OrgRoleRep.Creation?) : PiperEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/orgs/${enc(orgGuid)}/roles",
    body = rep
  )

  data class GetByOrgGuid(val orgGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/orgs/${enc(orgGuid)}/roles"
  )

  data class Patch(val orgGuid: UUID, val orgRoleGuid: UUID, val rep: OrgRoleRep.Update?) : PiperEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}",
    body = rep
  )

  data class Delete(val orgGuid: UUID, val orgRoleGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}"
  )
}
