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

internal class PostFeature @Inject constructor(
    private val featureService: FeatureService,
    private val featureMapper: FeatureMapper,
) : EndpointHandler<FeatureApi.Post, FeatureRep.Complete>(
    template = FeatureApi.Post::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): FeatureApi.Post =
      FeatureApi.Post(rep = call.getAndValidateBody())

  override suspend fun Handler.handle(endpoint: FeatureApi.Post): FeatureRep.Complete {
    val rep = endpoint.rep.required()
    auth(AuthSuperuser)
    val feature = featureService.create(featureMapper.model(rep))
    return featureMapper.completeRep(feature)
  }
}
