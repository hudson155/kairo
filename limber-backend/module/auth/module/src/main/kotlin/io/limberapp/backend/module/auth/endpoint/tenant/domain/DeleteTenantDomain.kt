package io.limberapp.backend.module.auth.endpoint.tenant.domain

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthAccountRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.tenant.TenantDomainApi
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.AccountRole
import java.util.*

internal class DeleteTenantDomain @Inject constructor(
    application: Application,
    private val tenantDomainService: TenantDomainService,
) : LimberApiEndpoint<TenantDomainApi.Delete, Unit>(
    application = application,
    endpointTemplate = TenantDomainApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = TenantDomainApi.Delete(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
      domain = call.parameters.getAsType(String::class, "domain")
  )

  override suspend fun Handler.handle(command: TenantDomainApi.Delete) {
    auth { AuthAccountRole(AccountRole.SUPERUSER) }
    tenantDomainService.delete(command.orgGuid, command.domain)
  }
}
