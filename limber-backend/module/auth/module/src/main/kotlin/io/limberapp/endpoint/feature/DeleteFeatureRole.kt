package io.limberapp.endpoint.feature

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.feature.FeatureRoleApi
import io.limberapp.auth.auth.AuthFeatureMember
import io.limberapp.exception.feature.FeatureRoleNotFound
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.feature.FeatureRoleService
import java.util.UUID

internal class DeleteFeatureRole @Inject constructor(
    private val featureRoleService: FeatureRoleService,
) : EndpointHandler<FeatureRoleApi.Delete, Unit>(
    template = FeatureRoleApi.Delete::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): FeatureRoleApi.Delete =
      FeatureRoleApi.Delete(featureRoleGuid = call.getParam(UUID::class, "featureRoleGuid"))

  override suspend fun Handler.handle(endpoint: FeatureRoleApi.Delete) {
    auth {
      val featureRole = featureRoleService[endpoint.featureRoleGuid] ?: throw FeatureRoleNotFound()
      AuthFeatureMember(featureRole.featureGuid, permission = OrgPermission.MANAGE_ORG_ROLES)
    }
    featureRoleService.delete(endpoint.featureRoleGuid)
  }
}
