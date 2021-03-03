package io.limberapp.endpoint.feature

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.feature.FeatureRoleApi
import io.limberapp.auth.auth.AuthFeatureMember
import io.limberapp.mapper.feature.FeatureRoleMapper
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.rep.feature.FeatureRoleRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.feature.FeatureRoleService
import java.util.UUID

internal class GetFeatureRolesByFeatureGuid @Inject constructor(
    private val featureRoleService: FeatureRoleService,
    private val featureRoleMapper: FeatureRoleMapper,
) : EndpointHandler<FeatureRoleApi.GetByFeatureGuid, Set<FeatureRoleRep.Complete>>(
    template = FeatureRoleApi.GetByFeatureGuid::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): FeatureRoleApi.GetByFeatureGuid =
      FeatureRoleApi.GetByFeatureGuid(featureGuid = call.getParam(UUID::class, "featureGuid"))

  override suspend fun Handler.handle(endpoint: FeatureRoleApi.GetByFeatureGuid): Set<FeatureRoleRep.Complete> {
    auth(AuthFeatureMember(endpoint.featureGuid, permission = OrgPermission.MANAGE_ORG_ROLES))
    val featureRoles = featureRoleService.getByFeatureGuid(endpoint.featureGuid)
    return featureRoles.map { featureRoleMapper.completeRep(it) }.toSet()
  }
}
