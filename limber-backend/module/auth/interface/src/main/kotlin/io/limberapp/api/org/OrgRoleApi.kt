package io.limberapp.api.org

import io.ktor.http.HttpMethod
import io.limberapp.rep.org.OrgRoleRep
import io.limberapp.restInterface.Endpoint
import java.util.UUID

object OrgRoleApi {
  data class Post(val rep: OrgRoleRep.Creation?) : Endpoint(
      httpMethod = HttpMethod.Post,
      rawPath = "/org-roles",
      body = rep,
  )

  data class GetByOrgGuid(val orgGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/org-roles",
      qp = listOf("orgGuid" to orgGuid.toString()),
  )

  data class Patch(val orgRoleGuid: UUID, val rep: OrgRoleRep.Update?) : Endpoint(
      httpMethod = HttpMethod.Patch,
      rawPath = "/org-roles/$orgRoleGuid",
      body = rep,
  )

  data class Delete(val orgRoleGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Delete,
      rawPath = "/org-roles/$orgRoleGuid",
  )
}
