package io.limberapp.backend.module.auth.api.org

import io.ktor.http.HttpMethod
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object OrgRoleApi {
  data class Post(val orgGuid: UUID, val rep: OrgRoleRep.Creation?) : LimberEndpoint(
      httpMethod = HttpMethod.Post,
      path = "/orgs/${enc(orgGuid)}/roles",
      body = rep
  )

  data class GetByOrgGuid(val orgGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/orgs/${enc(orgGuid)}/roles"
  )

  data class Patch(val orgGuid: UUID, val orgRoleGuid: UUID, val rep: OrgRoleRep.Update?) : LimberEndpoint(
      httpMethod = HttpMethod.Patch,
      path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}",
      body = rep
  )

  data class Delete(val orgGuid: UUID, val orgRoleGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Delete,
      path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}"
  )
}
