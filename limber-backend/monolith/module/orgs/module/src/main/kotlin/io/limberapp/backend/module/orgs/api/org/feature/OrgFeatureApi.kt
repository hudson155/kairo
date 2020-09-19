package io.limberapp.backend.module.orgs.api.org.feature

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object OrgFeatureApi {
  data class Post(val orgGuid: UUID, val rep: FeatureRep.Creation?) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/orgs/${enc(orgGuid)}/features",
    body = rep
  )

  data class Patch(val orgGuid: UUID, val featureGuid: UUID, val rep: FeatureRep.Update?) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/orgs/${enc(orgGuid)}/features/${enc(featureGuid)}",
    body = rep
  )

  data class Delete(val orgGuid: UUID, val featureGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/orgs/${enc(orgGuid)}/features/${enc(featureGuid)}"
  )
}
