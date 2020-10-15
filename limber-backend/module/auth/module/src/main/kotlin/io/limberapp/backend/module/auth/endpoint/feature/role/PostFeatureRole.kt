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

internal class PostFeatureRole @Inject constructor(
    application: Application,
    private val featureRoleService: FeatureRoleService,
    private val featureRoleMapper: FeatureRoleMapper,
) : LimberApiEndpoint<FeatureRoleApi.Post, FeatureRoleRep.Complete>(
    application = application,
    endpointTemplate = FeatureRoleApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = FeatureRoleApi.Post(
      featureGuid = call.parameters.getAsType(UUID::class, "featureGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: FeatureRoleApi.Post): FeatureRoleRep.Complete {
    val rep = command.rep.required()
    Authorization.FeatureMemberWithOrgPermission(command.featureGuid, OrgPermission.MANAGE_ORG_ROLES).authorize()
    val orgRole = featureRoleService.create(featureRoleMapper.model(command.featureGuid, rep))
    return featureRoleMapper.completeRep(orgRole)
  }
}
