package io.limberapp.api.tenant

import io.ktor.http.HttpMethod
import io.limberapp.rep.tenant.TenantRep
import io.limberapp.restInterface.Endpoint
import java.util.UUID

object TenantApi {
  data class Post(val rep: TenantRep.Creation?) : Endpoint(
      httpMethod = HttpMethod.Post,
      rawPath = "/tenants",
      body = rep,
  )

  data class Get(val orgGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/tenants/$orgGuid",
  )

  data class GetByDomain(val domain: String) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/tenants",
      qp = listOf("domain" to domain),
  )

  data class Patch(val orgGuid: UUID, val rep: TenantRep.Update?) : Endpoint(
      httpMethod = HttpMethod.Patch,
      rawPath = "/tenants/$orgGuid",
      body = rep,
  )

  data class Delete(val orgGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Delete,
      rawPath = "/tenants/$orgGuid",
  )
}
