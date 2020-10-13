package io.limberapp.backend.module.auth.api.org

import io.ktor.http.HttpMethod
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object OrgRoleMembershipApi {
  data class Post(val orgGuid: UUID, val orgRoleGuid: UUID, val rep: OrgRoleMembershipRep.Creation?) : LimberEndpoint(
      httpMethod = HttpMethod.Post,
      path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}/memberships",
      body = rep
  )

  data class GetByOrgRoleGuid(val orgGuid: UUID, val orgRoleGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}/memberships"
  )

  data class Delete(val orgGuid: UUID, val orgRoleGuid: UUID, val accountGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Delete,
      path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}/memberships/${enc(accountGuid)}"
  )
}
