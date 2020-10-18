package io.limberapp.backend.module.orgs.api.org

import io.ktor.http.HttpMethod
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object OrgApi {
  data class Post(val rep: OrgRep.Creation?) : LimberEndpoint(
      httpMethod = HttpMethod.Post,
      path = "/orgs",
      body = rep
  )

  data class Get(val orgGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/orgs/${enc(orgGuid)}"
  )

  data class GetByOwnerUserGuid(val ownerUserGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/orgs",
      queryParams = listOf("ownerUserGuid" to enc(ownerUserGuid))
  )

  data class Patch(val orgGuid: UUID, val rep: OrgRep.Update?) : LimberEndpoint(
      httpMethod = HttpMethod.Patch,
      path = "/orgs/${enc(orgGuid)}",
      body = rep
  )

  data class Delete(val orgGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Delete,
      path = "/orgs/${enc(orgGuid)}"
  )
}
