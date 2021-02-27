package io.limberapp.endpoint.org

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.org.OrgApi
import io.limberapp.auth.auth.AuthUser
import io.limberapp.exception.org.OrgNotFound
import io.limberapp.mapper.org.OrgMapper
import io.limberapp.rep.org.OrgRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.feature.FeatureService
import io.limberapp.service.org.OrgService
import java.util.UUID

internal class GetOrgByOwnerUserGuid @Inject constructor(
    private val featureService: FeatureService,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper,
) : EndpointHandler<OrgApi.GetByOwnerUserGuid, OrgRep.Complete>(
    template = OrgApi.GetByOwnerUserGuid::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgApi.GetByOwnerUserGuid =
      OrgApi.GetByOwnerUserGuid(ownerUserGuid = call.getParam(UUID::class, "ownerUserGuid"))

  override suspend fun Handler.handle(endpoint: OrgApi.GetByOwnerUserGuid): OrgRep.Complete {
    auth(AuthUser(endpoint.ownerUserGuid))
    val org = orgService.getByOwnerUserGuid(endpoint.ownerUserGuid) ?: throw OrgNotFound()
    val features = featureService.getByOrgGuid(org.guid)
    return orgMapper.completeRep(org, features)
  }
}
