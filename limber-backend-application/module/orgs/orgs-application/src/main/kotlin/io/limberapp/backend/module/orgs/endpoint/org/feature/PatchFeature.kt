package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.feature.OrgFeatureApi
import io.limberapp.backend.module.orgs.mapper.feature.FeatureMapper
import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.service.org.FeatureService
import java.util.*

internal class PatchFeature @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val featureService: FeatureService,
  private val featureMapper: FeatureMapper
) : LimberApiEndpoint<OrgFeatureApi.Patch, FeatureRep.Complete>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = OrgFeatureApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgFeatureApi.Patch(
    orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
    featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: OrgFeatureApi.Patch): FeatureRep.Complete {
    val rep = command.rep.required()
    Authorization.OrgMemberWithPermission(command.orgGuid, OrgPermission.MANAGE_ORG_FEATURES).authorize()
    val feature = featureService.update(command.orgGuid, command.featureGuid, featureMapper.update(rep))
    return featureMapper.completeRep(feature)
  }
}
