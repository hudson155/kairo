package io.limberapp.endpoint.tenant

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.tenant.TenantApi
import io.limberapp.auth.Auth
import io.limberapp.exception.tenant.TenantNotFound
import io.limberapp.mapper.tenant.TenantMapper
import io.limberapp.rep.tenant.TenantRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.tenant.TenantDomainService
import io.limberapp.service.tenant.TenantService
import java.util.UUID

internal class GetTenant @Inject constructor(
    private val tenantService: TenantService,
    private val tenantDomainService: TenantDomainService,
    private val tenantMapper: TenantMapper,
) : EndpointHandler<TenantApi.Get, TenantRep.Complete>(
    template = TenantApi.Get::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TenantApi.Get =
      TenantApi.Get(orgGuid = call.getParam(UUID::class, "orgGuid"))

  override suspend fun Handler.handle(endpoint: TenantApi.Get): TenantRep.Complete {
    auth(Auth.Allow)
    val tenant = tenantService[endpoint.orgGuid] ?: throw TenantNotFound()
    val domains = tenantDomainService.getByOrgGuid(endpoint.orgGuid)
    return tenantMapper.completeRep(tenant, domains)
  }
}
