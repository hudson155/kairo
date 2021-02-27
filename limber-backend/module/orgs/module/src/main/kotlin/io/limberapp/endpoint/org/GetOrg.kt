package io.limberapp.endpoint.org

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.org.OrgApi
import io.limberapp.auth.auth.AuthOrgMember
import io.limberapp.exception.org.OrgNotFound
import io.limberapp.mapper.org.OrgMapper
import io.limberapp.rep.org.OrgRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.feature.FeatureService
import io.limberapp.service.org.OrgService
import java.util.UUID

internal class GetOrg @Inject constructor(
    private val featureService: FeatureService,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper,
) : EndpointHandler<OrgApi.Get, OrgRep.Complete>(
    template = OrgApi.Get::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgApi.Get =
      OrgApi.Get(orgGuid = call.getParam(UUID::class, "orgGuid"))

  override suspend fun Handler.handle(endpoint: OrgApi.Get): OrgRep.Complete {
    auth(AuthOrgMember(endpoint.orgGuid))
    val org = orgService[endpoint.orgGuid] ?: throw OrgNotFound()
    val features = featureService.getByOrgGuid(endpoint.orgGuid)
    return orgMapper.completeRep(org, features)
  }
}
