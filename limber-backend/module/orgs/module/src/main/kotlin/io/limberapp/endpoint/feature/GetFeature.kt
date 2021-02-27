package io.limberapp.endpoint.feature

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.feature.FeatureApi
import io.limberapp.auth.auth.AuthFeatureMember
import io.limberapp.exception.feature.FeatureNotFound
import io.limberapp.mapper.feature.FeatureMapper
import io.limberapp.rep.feature.FeatureRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.feature.FeatureService
import java.util.UUID

internal class GetFeature @Inject constructor(
    private val featureService: FeatureService,
    private val featureMapper: FeatureMapper,
) : EndpointHandler<FeatureApi.Get, FeatureRep.Complete>(
    template = FeatureApi.Get::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): FeatureApi.Get =
      FeatureApi.Get(featureGuid = call.getParam(UUID::class, "featureGuid"))

  override suspend fun Handler.handle(endpoint: FeatureApi.Get): FeatureRep.Complete {
    auth(AuthFeatureMember(endpoint.featureGuid))
    val feature = featureService[endpoint.featureGuid] ?: throw FeatureNotFound()
    return featureMapper.completeRep(feature)
  }
}
