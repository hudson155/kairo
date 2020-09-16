package io.limberapp.backend.module.auth.api.feature.role

import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc

@Suppress("StringLiteralDuplication")
object FeatureRoleApi {
  data class Post(val featureGuid: String, val rep: FeatureRoleRep.Creation?) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/features/${enc(featureGuid)}/roles",
    body = rep
  )

  data class GetByFeatureGuid(val featureGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/features/${enc(featureGuid)}/roles"
  )

  data class Patch(
    val featureGuid: String,
    val featureRoleGuid: String,
    val rep: FeatureRoleRep.Update?,
  ) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/features/${enc(featureGuid)}/roles/${enc(featureRoleGuid)}",
    body = rep
  )

  data class Delete(val featureGuid: String, val featureRoleGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/features/${enc(featureGuid)}/roles/${enc(featureRoleGuid)}"
  )
}
