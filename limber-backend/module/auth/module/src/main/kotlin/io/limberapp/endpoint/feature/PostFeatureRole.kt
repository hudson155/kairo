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

internal class PostFeatureRole @Inject constructor(
    private val featureRoleService: FeatureRoleService,
    private val featureRoleMapper: FeatureRoleMapper,
) : EndpointHandler<FeatureRoleApi.Post, FeatureRoleRep.Complete>(
    template = FeatureRoleApi.Post::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): FeatureRoleApi.Post =
      FeatureRoleApi.Post(rep = call.getAndValidateBody())

  override suspend fun Handler.handle(endpoint: FeatureRoleApi.Post): FeatureRoleRep.Complete {
    val rep = endpoint.rep.required()
    auth(AuthFeatureMember(rep.featureGuid, permission = OrgPermission.MANAGE_ORG_ROLES))
    val featureRole = featureRoleService.create(featureRoleMapper.model(rep))
    return featureRoleMapper.completeRep(featureRole)
  }
}
