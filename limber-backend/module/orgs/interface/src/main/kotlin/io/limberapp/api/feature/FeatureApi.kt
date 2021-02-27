package io.limberapp.api.feature

import io.ktor.http.HttpMethod
import io.limberapp.rep.feature.FeatureRep
import io.limberapp.restInterface.Endpoint
import java.util.UUID

object FeatureApi {
  data class Post(val orgGuid: UUID, val rep: FeatureRep.Creation?) : Endpoint(
      httpMethod = HttpMethod.Post,
      rawPath = "/orgs/$orgGuid/features",
      body = rep,
  )

  data class Get(val featureGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Get,
      rawPath = "/features/$featureGuid",
  )

  data class Patch(val featureGuid: UUID, val rep: FeatureRep.Update?) : Endpoint(
      httpMethod = HttpMethod.Patch,
      rawPath = "/features/$featureGuid",
      body = rep,
  )

  data class Delete(val featureGuid: UUID) : Endpoint(
      httpMethod = HttpMethod.Delete,
      rawPath = "/features/$featureGuid",
  )
}
