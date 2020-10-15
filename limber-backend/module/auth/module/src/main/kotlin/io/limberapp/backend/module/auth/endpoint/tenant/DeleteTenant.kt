package io.limberapp.backend.module.auth.endpoint.tenant

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.AccountRole
import java.util.*

internal class DeleteTenant @Inject constructor(
    application: Application,
    private val tenantService: TenantService,
) : LimberApiEndpoint<TenantApi.Delete, Unit>(
    application = application,
    endpointTemplate = TenantApi.Delete::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = TenantApi.Delete(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid")
  )

  override suspend fun Handler.handle(command: TenantApi.Delete) {
    Authorization.Role(AccountRole.SUPERUSER).authorize()
    tenantService.delete(command.orgGuid)
  }
}
