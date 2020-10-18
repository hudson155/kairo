package io.limberapp.backend.module.auth.api.tenant

import io.ktor.http.HttpMethod
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object TenantApi {
  data class Post(val rep: TenantRep.Creation?) : LimberEndpoint(
      httpMethod = HttpMethod.Post,
      path = "/tenants",
      body = rep
  )

  data class Get(val orgGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/tenants/${enc(orgGuid)}"
  )

  data class GetByDomain(val domain: String) : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/tenants",
      queryParams = listOf("domain" to enc(domain))
  )

  data class Patch(val orgGuid: UUID, val rep: TenantRep.Update?) : LimberEndpoint(
      httpMethod = HttpMethod.Patch,
      path = "/tenants/${enc(orgGuid)}",
      body = rep
  )

  data class Delete(val orgGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Delete,
      path = "/tenants/${enc(orgGuid)}"
  )
}
