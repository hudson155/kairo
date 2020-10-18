package io.limberapp.backend.module.orgs.api.feature

import io.ktor.http.HttpMethod
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object FeatureApi {
  data class Post(val orgGuid: UUID, val rep: FeatureRep.Creation?) : LimberEndpoint(
      httpMethod = HttpMethod.Post,
      path = "/orgs/${enc(orgGuid)}/features",
      body = rep,
  )

  data class Get(val featureGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/features/${enc(featureGuid)}",
  )

  data class Patch(val orgGuid: UUID, val featureGuid: UUID, val rep: FeatureRep.Update?) : LimberEndpoint(
      httpMethod = HttpMethod.Patch,
      path = "/orgs/${enc(orgGuid)}/features/${enc(featureGuid)}",
      body = rep,
  )

  data class Delete(val orgGuid: UUID, val featureGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Delete,
      path = "/orgs/${enc(orgGuid)}/features/${enc(featureGuid)}",
  )
}
