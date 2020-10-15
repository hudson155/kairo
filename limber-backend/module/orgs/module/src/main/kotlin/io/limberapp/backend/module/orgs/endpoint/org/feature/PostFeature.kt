package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.mapper.feature.FeatureMapper
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.service.feature.FeatureService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.orgPermissions.OrgPermission
import java.util.*

internal class PostFeature @Inject constructor(
    application: Application,
    private val featureService: FeatureService,
    private val featureMapper: FeatureMapper,
) : LimberApiEndpoint<FeatureApi.Post, FeatureRep.Complete>(
    application = application,
    endpointTemplate = FeatureApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FeatureApi.Post(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FeatureApi.Post): FeatureRep.Complete {
    val rep = command.rep.required()
    Authorization.OrgMemberWithPermission(command.orgGuid, OrgPermission.MANAGE_ORG_FEATURES).authorize()
    val feature = featureService.create(featureMapper.model(command.orgGuid, rep))
    return featureMapper.completeRep(feature)
  }
}
