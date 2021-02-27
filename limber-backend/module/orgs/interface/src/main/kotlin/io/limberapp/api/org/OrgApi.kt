package io.limberapp.api.org

import io.ktor.http.HttpMethod
import io.limberapp.rep.org.OrgRep
import io.limberapp.restInterface.Endpoint
import java.util.UUID

object OrgApi {
  data class Post(val rep: OrgRep.Creation?) : Endpoint(
      httpMethod = HttpMethod.Post,
      rawPath = "/orgs",
      body = rep,
  )

  data class Get(val orgGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/orgs/$orgGuid",
  )

  data class GetByOwnerUserGuid(val ownerUserGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/orgs",
      qp = listOf("ownerUserGuid" to ownerUserGuid.toString()),
  )

  data class Patch(val orgGuid: UUID, val rep: OrgRep.Update?) : Endpoint(
      httpMethod = HttpMethod.Patch,
      rawPath = "/orgs/$orgGuid",
      body = rep,
  )

  data class Delete(val orgGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Delete,
      rawPath = "/orgs/$orgGuid",
  )
}
