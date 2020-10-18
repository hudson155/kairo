package io.limberapp.backend.module.auth.api.feature

import io.ktor.http.HttpMethod
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object FeatureRoleApi {
  data class Post(val featureGuid: UUID, val rep: FeatureRoleRep.Creation?) : LimberEndpoint(
      httpMethod = HttpMethod.Post,
      path = "/features/${enc(featureGuid)}/roles",
      body = rep
  )

  data class GetByFeatureGuid(val featureGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/features/${enc(featureGuid)}/roles"
  )

  data class Patch(val featureGuid: UUID, val featureRoleGuid: UUID, val rep: FeatureRoleRep.Update?) : LimberEndpoint(
      httpMethod = HttpMethod.Patch,
      path = "/features/${enc(featureGuid)}/roles/${enc(featureRoleGuid)}",
      body = rep
  )

  data class Delete(val featureGuid: UUID, val featureRoleGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Delete,
      path = "/features/${enc(featureGuid)}/roles/${enc(featureRoleGuid)}"
  )
}
