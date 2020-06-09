package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.permissions.orgPermissions.OrgPermission
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.mapper.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.*

/**
 * Updates an org's information.
 */
internal class PatchOrg @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val featureService: FeatureService,
  private val orgService: OrgService,
  private val orgMapper: OrgMapper
) : LimberApiEndpoint<OrgApi.Patch, OrgRep.Complete>(
  application = application,
  pathPrefix = servingConfig.apiPathPrefix,
  endpointTemplate = OrgApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgApi.Patch(
    orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: OrgApi.Patch): OrgRep.Complete {
    Authorization.OrgMemberWithPermission(command.orgGuid, OrgPermission.MANAGE_ORG_METADATA).authorize()
    val update = orgMapper.update(command.rep.required())
    val org = orgService.update(command.orgGuid, update)
    val features = featureService.getByOrgGuid(org.guid)
    return orgMapper.completeRep(org, features)
  }
}
