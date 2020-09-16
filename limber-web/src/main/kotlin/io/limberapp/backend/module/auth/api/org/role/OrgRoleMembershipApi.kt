package io.limberapp.backend.module.auth.api.org.role

import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc

@Suppress("StringLiteralDuplication")
object OrgRoleMembershipApi {
  data class Post(
    val orgGuid: String,
    val orgRoleGuid: String,
    val rep: OrgRoleMembershipRep.Creation?,
  ) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}/memberships",
    body = rep
  )

  data class GetByOrgRoleGuid(val orgGuid: String, val orgRoleGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}/memberships"
  )

  data class Delete(val orgGuid: String, val orgRoleGuid: String, val accountGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}/memberships/${enc(accountGuid)}"
  )
}
