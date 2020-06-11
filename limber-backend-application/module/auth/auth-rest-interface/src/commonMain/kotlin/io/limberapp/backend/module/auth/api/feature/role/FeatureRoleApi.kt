package io.limberapp.backend.module.auth.api.feature.role

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep

@Suppress("StringLiteralDuplication")
object FeatureRoleApi {
  data class Post(val featureGuid: UUID, val rep: FeatureRoleRep.Creation?) : PiperEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/features/${enc(featureGuid)}/roles",
    body = rep
  )

  data class GetByFeatureGuid(val featureGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/features/${enc(featureGuid)}/roles"
  )

  data class Patch(val featureGuid: UUID, val featureRoleGuid: UUID, val rep: FeatureRoleRep.Update?) : PiperEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/features/${enc(featureGuid)}/roles/${enc(featureRoleGuid)}",
    body = rep
  )

  data class Delete(val featureGuid: UUID, val featureRoleGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/features/${enc(featureGuid)}/roles/${enc(featureRoleGuid)}"
  )
}
