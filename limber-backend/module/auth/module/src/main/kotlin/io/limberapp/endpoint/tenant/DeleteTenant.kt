package io.limberapp.endpoint.tenant

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.tenant.TenantApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.tenant.TenantService
import java.util.UUID

internal class DeleteTenant @Inject constructor(
    private val tenantService: TenantService,
) : EndpointHandler<TenantApi.Delete, Unit>(
    template = TenantApi.Delete::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TenantApi.Delete =
      TenantApi.Delete(orgGuid = call.getParam(UUID::class, "orgGuid"))

  override suspend fun Handler.handle(endpoint: TenantApi.Delete) {
    auth(AuthSuperuser)
    tenantService.delete(endpoint.orgGuid)
  }
}
