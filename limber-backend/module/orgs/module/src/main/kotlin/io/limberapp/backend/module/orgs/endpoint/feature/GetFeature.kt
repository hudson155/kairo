package io.limberapp.backend.module.orgs.endpoint.feature

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.exception.feature.FeatureNotFound
import io.limberapp.backend.module.orgs.mapper.feature.FeatureMapper
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.service.feature.FeatureService
import io.limberapp.common.restInterface.template
import java.util.*

internal class GetFeature @Inject constructor(
    application: Application,
    private val featureService: FeatureService,
    private val featureMapper: FeatureMapper,
) : LimberApiEndpoint<FeatureApi.Get, FeatureRep.Complete>(
    application = application,
    endpointTemplate = FeatureApi.Get::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FeatureApi.Get(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid")
  )

  override suspend fun Handler.handle(command: FeatureApi.Get): FeatureRep.Complete {
    auth { AuthFeatureMember(command.featureGuid) }
    val feature = featureService.get(command.featureGuid) ?: throw FeatureNotFound()
    return featureMapper.completeRep(feature)
  }
}
