package io.limberapp.endpoint.tenant

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.tenant.TenantDomainApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.tenant.TenantDomainService
import java.util.UUID

internal class DeleteTenantDomain @Inject constructor(
    private val tenantDomainService: TenantDomainService,
) : EndpointHandler<TenantDomainApi.Delete, Unit>(
    template = TenantDomainApi.Delete::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TenantDomainApi.Delete =
      TenantDomainApi.Delete(
          orgGuid = call.getParam(UUID::class, "orgGuid"),
          domain = call.getParam(String::class, "domain"),
      )

  override suspend fun Handler.handle(endpoint: TenantDomainApi.Delete) {
    auth(AuthSuperuser)
    tenantDomainService.delete(endpoint.orgGuid, endpoint.domain)
  }
}
