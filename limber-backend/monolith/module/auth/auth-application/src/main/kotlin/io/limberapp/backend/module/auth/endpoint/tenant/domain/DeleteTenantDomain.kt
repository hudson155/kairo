package io.limberapp.backend.module.auth.endpoint.tenant.domain

import com.google.inject.Inject
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.tenant.domain.TenantDomainApi
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
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
    Authorization.Role(JwtRole.SUPERUSER).authorize()
    tenantDomainService.delete(command.orgGuid, command.domain)
  }
}
