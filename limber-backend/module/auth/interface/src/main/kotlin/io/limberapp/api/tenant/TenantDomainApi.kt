package io.limberapp.api.tenant

import io.ktor.http.HttpMethod
import io.limberapp.rep.tenant.TenantDomainRep
import io.limberapp.restInterface.Endpoint
import java.util.UUID

object TenantDomainApi {
  data class Post(val orgGuid: UUID, val rep: TenantDomainRep.Creation?) : Endpoint(
      httpMethod = HttpMethod.Post,
      rawPath = "/tenants/$orgGuid/domains",
      body = rep,
  )

  data class Delete(val orgGuid: UUID, val domain: String) : Endpoint(
      httpMethod = HttpMethod.Delete,
      rawPath = "/tenants/$orgGuid/domains/$domain",
  )
}
