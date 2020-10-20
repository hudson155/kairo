package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Auth
import io.limberapp.backend.authorization.authorization.AuthOrgMember
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.mapper.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.feature.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.orgPermissions.OrgPermission
import java.util.*

internal class PatchOrg @Inject constructor(
    application: Application,
    private val featureService: FeatureService,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper,
) : LimberApiEndpoint<OrgApi.Patch, OrgRep.Complete>(
    application = application,
    endpointTemplate = OrgApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgApi.Patch(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: OrgApi.Patch): OrgRep.Complete {
    val rep = command.rep.required()
    auth {
      Auth.All(
          AuthOrgMember(command.orgGuid, permission = OrgPermission.MANAGE_ORG_METADATA),
          rep.ownerUserGuid?.let { AuthOrgMember(command.orgGuid, isOwner = true) },
      )
    }
    val org = orgService.update(command.orgGuid, orgMapper.update(rep))
    val features = featureService.getByOrgGuid(org.guid)
    return orgMapper.completeRep(org, features)
  }
}
