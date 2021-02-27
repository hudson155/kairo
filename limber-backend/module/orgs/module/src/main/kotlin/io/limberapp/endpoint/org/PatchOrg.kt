package io.limberapp.endpoint.org

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.org.OrgApi
import io.limberapp.auth.Auth
import io.limberapp.auth.auth.AuthOrgMember
import io.limberapp.mapper.org.OrgMapper
import io.limberapp.permissions.org.OrgPermission
import io.limberapp.rep.org.OrgRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.feature.FeatureService
import io.limberapp.service.org.OrgService
import java.util.UUID

internal class PatchOrg @Inject constructor(
    private val featureService: FeatureService,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper,
) : EndpointHandler<OrgApi.Patch, OrgRep.Complete>(
    template = OrgApi.Patch::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgApi.Patch =
      OrgApi.Patch(
          orgGuid = call.getParam(UUID::class, "orgGuid"),
          rep = call.getAndValidateBody(),
      )

  override suspend fun Handler.handle(endpoint: OrgApi.Patch): OrgRep.Complete {
    val rep = endpoint.rep.required()
    authAll(
        AuthOrgMember(endpoint.orgGuid, permission = OrgPermission.MANAGE_ORG_METADATA),
        if (rep.ownerUserGuid != null) AuthOrgMember(endpoint.orgGuid, isOwner = true)
        else Auth.Allow,
    )
    val org = orgService.update(endpoint.orgGuid, orgMapper.update(rep))
    val features = featureService.getByOrgGuid(org.guid)
    return orgMapper.completeRep(org, features)
  }
}
