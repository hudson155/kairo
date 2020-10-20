package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthOrgMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.feature.FeatureApi
import io.limberapp.backend.module.orgs.mapper.feature.FeatureMapper
import io.limberapp.backend.module.orgs.rep.feature.FeatureRep
import io.limberapp.backend.module.orgs.service.feature.FeatureService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.orgPermissions.OrgPermission
import java.util.*

internal class PatchFeature @Inject constructor(
    application: Application,
    private val featureService: FeatureService,
    private val featureMapper: FeatureMapper,
) : LimberApiEndpoint<FeatureApi.Patch, FeatureRep.Complete>(
    application = application,
    endpointTemplate = FeatureApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FeatureApi.Patch(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FeatureApi.Patch): FeatureRep.Complete {
    val rep = command.rep.required()
    auth { AuthOrgMember(command.orgGuid, permission = OrgPermission.MANAGE_ORG_FEATURES) }
    val feature = featureService.update(command.orgGuid, command.featureGuid, featureMapper.update(rep))
    return featureMapper.completeRep(feature)
  }
}
