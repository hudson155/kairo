package io.limberapp.backend.module.orgs.api.org.feature

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.orgs.rep.org.FeatureRep

@Suppress("StringLiteralDuplication")
object OrgFeatureApi {
  data class Post(val orgGuid: UUID, val rep: FeatureRep.Creation?) : PiperEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/orgs/${enc(orgGuid)}/features",
    body = rep
  )

  data class Patch(val orgGuid: UUID, val featureGuid: UUID, val rep: FeatureRep.Update?) : PiperEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/orgs/${enc(orgGuid)}/features/${enc(featureGuid)}",
    body = rep
  )

  data class Delete(val orgGuid: UUID, val featureGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/orgs/${enc(orgGuid)}/features/${enc(featureGuid)}"
  )
}
