package io.limberapp.backend.module.auth.endpoint.feature.role

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.feature.role.FeatureRoleApi
import io.limberapp.backend.module.auth.mapper.feature.FeatureRoleMapper
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.backend.module.auth.service.feature.FeatureRoleService
import io.limberapp.common.restInterface.template
import java.util.*

internal class GetFeatureRolesByFeatureGuid @Inject constructor(
  application: Application,
  private val featureRoleService: FeatureRoleService,
  private val featureRoleMapper: FeatureRoleMapper,
) : LimberApiEndpoint<FeatureRoleApi.GetByFeatureGuid, Set<FeatureRoleRep.Complete>>(
  application = application,
  endpointTemplate = FeatureRoleApi.GetByFeatureGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FeatureRoleApi.GetByFeatureGuid(
    featureGuid = call.parameters.getAsType(UUID::class, "featureGuid")
  )

  override suspend fun Handler.handle(command: FeatureRoleApi.GetByFeatureGuid): Set<FeatureRoleRep.Complete> {
    Authorization.FeatureMemberWithOrgPermission(command.featureGuid, OrgPermission.MANAGE_ORG_ROLES).authorize()
    val featureRoles = featureRoleService.findAsSet { featureGuid(command.featureGuid) }
    return featureRoles.map { featureRoleMapper.completeRep(it) }.toSet()
  }
}
