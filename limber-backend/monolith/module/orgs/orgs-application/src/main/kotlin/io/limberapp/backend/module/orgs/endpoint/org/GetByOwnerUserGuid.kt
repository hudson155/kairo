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

internal class GetByOwnerUserGuid @Inject constructor(
  application: Application,
  private val featureService: FeatureService,
  private val orgService: OrgService,
  private val orgMapper: OrgMapper,
) : LimberApiEndpoint<OrgApi.GetByOwnerUserGuid, OrgRep.Complete>(
  application = application,
  endpointTemplate = OrgApi.GetByOwnerUserGuid::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgApi.GetByOwnerUserGuid(
    ownerUserGuid = call.parameters.getAsType(UUID::class, "ownerUserGuid")
  )

  override suspend fun Handler.handle(command: OrgApi.GetByOwnerUserGuid): OrgRep.Complete {
    Authorization.User(command.ownerUserGuid).authorize()
    val org = orgService.findOnlyOrNull { ownerUserGuid(command.ownerUserGuid) } ?: throw OrgNotFound()
    val features = featureService.findAsList { orgGuid(org.guid) }
    return orgMapper.completeRep(org, features)
  }
}
