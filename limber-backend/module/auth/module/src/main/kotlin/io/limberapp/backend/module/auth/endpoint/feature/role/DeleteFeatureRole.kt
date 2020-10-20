package io.limberapp.backend.module.auth.endpoint.feature.role

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthFeatureMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.feature.FeatureRoleApi
import io.limberapp.backend.module.auth.service.feature.FeatureRoleService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.orgPermissions.OrgPermission
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
    auth { AuthFeatureMember(command.featureGuid, permission = OrgPermission.MANAGE_ORG_ROLES) }
    featureRoleService.delete(command.featureGuid, command.featureRoleGuid)
  }
}
