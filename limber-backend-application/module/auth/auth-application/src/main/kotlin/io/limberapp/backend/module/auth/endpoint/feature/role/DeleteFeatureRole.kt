package io.limberapp.backend.module.auth.endpoint.feature.role

import com.google.inject.Inject
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.feature.role.FeatureRoleApi
import io.limberapp.backend.module.auth.service.feature.FeatureRoleService
import java.util.*

internal class DeleteFeatureRole @Inject constructor(
  application: Application,
  private val featureRoleService: FeatureRoleService,
) : LimberApiEndpoint<FeatureRoleApi.Delete, Unit>(
  application = application,
  endpointTemplate = FeatureRoleApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FeatureRoleApi.Delete(
    featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
    featureRoleGuid = call.parameters.getAsType(UUID::class, "featureRoleGuid")
  )

  override suspend fun Handler.handle(command: FeatureRoleApi.Delete) {
    Authorization.FeatureMemberWithOrgPermission(command.featureGuid, OrgPermission.MANAGE_ORG_ROLES).authorize()
    featureRoleService.delete(command.featureGuid, command.featureRoleGuid)
  }
}
