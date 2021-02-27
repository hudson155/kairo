package io.limberapp.endpoint.feature

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.feature.FeatureApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.mapper.feature.FeatureMapper
import io.limberapp.rep.feature.FeatureRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.feature.FeatureService
import java.util.UUID

internal class PatchFeature @Inject constructor(
    private val featureService: FeatureService,
    private val featureMapper: FeatureMapper,
) : EndpointHandler<FeatureApi.Patch, FeatureRep.Complete>(
    template = FeatureApi.Patch::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): FeatureApi.Patch =
      FeatureApi.Patch(
          featureGuid = call.getParam(UUID::class, "featureGuid"),
          rep = call.getAndValidateBody(),
      )

  override suspend fun Handler.handle(endpoint: FeatureApi.Patch): FeatureRep.Complete {
    val rep = endpoint.rep.required()
    auth(AuthSuperuser)
    val feature = featureService.update(endpoint.featureGuid, featureMapper.update(rep))
    return featureMapper.completeRep(feature)
  }
}
