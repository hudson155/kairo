package io.limberapp.api.org

import io.ktor.http.HttpMethod
import io.limberapp.rep.org.OrgRoleMembershipRep
import io.limberapp.restInterface.Endpoint
import java.util.UUID

object OrgRoleMembershipApi {
  data class Post(val orgRoleGuid: UUID, val rep: OrgRoleMembershipRep.Creation?) : Endpoint(
      httpMethod = HttpMethod.Post,
      rawPath = "/org-roles/$orgRoleGuid/memberships",
      body = rep,
  )

  data class GetByOrgRoleGuid(val orgRoleGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/org-roles/$orgRoleGuid/memberships",
  )

  data class Delete(val orgRoleGuid: UUID, val userGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Delete,
      rawPath = "/org-roles/$orgRoleGuid/memberships/$userGuid",
  )
}
