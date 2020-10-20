package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthAccountRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.mapper.org.OrgMapper
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.AccountRole

internal class PostOrg @Inject constructor(
    application: Application,
    private val orgService: OrgService,
    private val orgMapper: OrgMapper,
) : LimberApiEndpoint<OrgApi.Post, OrgRep.Complete>(
    application = application,
    endpointTemplate = OrgApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgApi.Post(
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: OrgApi.Post): OrgRep.Complete {
    val rep = command.rep.required()
    auth { AuthAccountRole(AccountRole.SUPERUSER) }
    val org = orgService.create(orgMapper.model(rep))
    return orgMapper.completeRep(org, emptyList())
  }
}
