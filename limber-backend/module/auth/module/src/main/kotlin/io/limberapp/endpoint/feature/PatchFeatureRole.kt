package io.limberapp.endpoint.feature

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.feature.FeatureRoleApi
import io.limberapp.auth.auth.AuthFeatureMember
import io.limberapp.exception.feature.FeatureRoleNotFound
import io.limberapp.mapper.feature.FeatureRoleMapper
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.rep.feature.FeatureRoleRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.feature.FeatureRoleService
import java.util.UUID

internal class PatchFeatureRole @Inject constructor(
    private val featureRoleService: FeatureRoleService,
    private val featureRoleMapper: FeatureRoleMapper,
) : EndpointHandler<FeatureRoleApi.Patch, FeatureRoleRep.Complete>(
    template = FeatureRoleApi.Patch::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): FeatureRoleApi.Patch =
      FeatureRoleApi.Patch(
          featureRoleGuid = call.getParam(UUID::class, "featureRoleGuid"),
          rep = call.getAndValidateBody(),
      )

  override suspend fun Handler.handle(endpoint: FeatureRoleApi.Patch): FeatureRoleRep.Complete {
    val rep = endpoint.rep.required()
    auth {
      val featureRole = featureRoleService[endpoint.featureRoleGuid] ?: throw FeatureRoleNotFound()
      AuthFeatureMember(featureRole.featureGuid, permission = OrgPermission.MANAGE_ORG_ROLES)
    }
    val featureRole = featureRoleService.update(
        featureRoleGuid = endpoint.featureRoleGuid,
        update = featureRoleMapper.update(rep),
    )
    return featureRoleMapper.completeRep(featureRole)
  }
}
