package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.mapper.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService

/**
 * Creates a new org.
 */
internal class PostOrg @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val featureService: FeatureService,
  private val orgService: OrgService,
  private val orgMapper: OrgMapper
) : LimberApiEndpoint<OrgApi.Post, OrgRep.Complete>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = OrgApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgApi.Post(
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: OrgApi.Post): OrgRep.Complete {
    Authorization.Role(JwtRole.SUPERUSER).authorize()
    val org = orgService.create(orgMapper.model(command.rep.required()))
    val features = featureService.createDefaults(org.guid)
    return orgMapper.completeRep(org, features)
  }
}
