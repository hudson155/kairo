package io.limberapp.api.feature

import io.ktor.http.HttpMethod
import io.limberapp.rep.feature.FeatureRoleRep
import io.limberapp.restInterface.Endpoint
import java.util.UUID

object FeatureRoleApi {
  data class Post(val rep: FeatureRoleRep.Creation?) : Endpoint(
      httpMethod = HttpMethod.Post,
      rawPath = "/feature-roles",
      body = rep,
  )

  data class GetByFeatureGuid(val featureGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/feature-roles",
      qp = listOf("featureGuid" to featureGuid.toString()),
  )

  data class Patch(val featureRoleGuid: UUID, val rep: FeatureRoleRep.Update?) : Endpoint(
      httpMethod = HttpMethod.Patch,
      rawPath = "/feature-roles/$featureRoleGuid",
      body = rep,
  )

  data class Delete(val featureRoleGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Delete,
      rawPath = "/feature-roles/$featureRoleGuid",
  )
}
