package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthAccountRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.AccountRole
import java.util.*

internal class DeleteOrg @Inject constructor(
    application: Application,
    private val orgService: OrgService,
) : LimberApiEndpoint<OrgApi.Delete, Unit>(
    application = application,
    endpointTemplate = OrgApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = OrgApi.Delete(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid")
  )

  override suspend fun Handler.handle(command: OrgApi.Delete) {
    auth { AuthAccountRole(AccountRole.SUPERUSER) }
    orgService.delete(command.orgGuid)
  }
}
