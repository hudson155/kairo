package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.mapper.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.common.restInterface.template
import java.util.*

internal class GetOrg @Inject constructor(
  application: Application,
  private val featureService: FeatureService,
  private val orgService: OrgService,
  private val orgMapper: OrgMapper,
) : LimberApiEndpoint<OrgApi.Get, OrgRep.Complete>(
  application = application,
  endpointTemplate = OrgApi.Get::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgApi.Get(
    orgGuid = call.parameters.getAsType(UUID::class, "orgGuid")
  )

  override suspend fun Handler.handle(command: OrgApi.Get): OrgRep.Complete {
    Authorization.OrgMember(command.orgGuid).authorize()
    val org = orgService.findOnlyOrNull { orgGuid(command.orgGuid) } ?: throw OrgNotFound()
    val features = featureService.findAsList { orgGuid(command.orgGuid) }
    return orgMapper.completeRep(org, features)
  }
}
