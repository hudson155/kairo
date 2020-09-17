package io.limberapp.backend.module.auth.api.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object TenantApi {
  data class Post(val rep: TenantRep.Creation?) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/tenants",
    body = rep
  )

  data class Get(val orgGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/tenants/${enc(orgGuid)}"
  )

  data class GetByDomain(val domain: String) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/tenants",
    queryParams = listOf("domain" to enc(domain))
  )

  data class Patch(val orgGuid: UUID, val rep: TenantRep.Update?) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/tenants/${enc(orgGuid)}",
    body = rep
  )

  data class Delete(val orgGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/tenants/${enc(orgGuid)}"
  )
}
