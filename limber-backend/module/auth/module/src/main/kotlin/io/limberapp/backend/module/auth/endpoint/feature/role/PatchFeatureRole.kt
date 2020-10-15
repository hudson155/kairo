package io.limberapp.backend.module.auth.endpoint.feature.role

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.feature.FeatureRoleApi
import io.limberapp.backend.module.auth.mapper.feature.FeatureRoleMapper
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.backend.module.auth.service.feature.FeatureRoleService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.orgPermissions.OrgPermission
import java.util.*

internal class PatchFeatureRole @Inject constructor(
    application: Application,
    private val featureRoleService: FeatureRoleService,
    private val featureRoleMapper: FeatureRoleMapper,
) : LimberApiEndpoint<FeatureRoleApi.Patch, FeatureRoleRep.Complete>(
    application = application,
    endpointTemplate = FeatureRoleApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FeatureRoleApi.Patch(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      featureRoleGuid = call.parameters.getAsType(UUID::class, "featureRoleGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FeatureRoleApi.Patch): FeatureRoleRep.Complete {
    val rep = command.rep.required()
    Authorization.FeatureMemberWithOrgPermission(command.featureGuid, OrgPermission.MANAGE_ORG_ROLES).authorize()
    val orgRole = featureRoleService.update(command.featureGuid, command.featureRoleGuid, featureRoleMapper.update(rep))
    return featureRoleMapper.completeRep(orgRole)
  }
}
