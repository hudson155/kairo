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

internal class GetTenantByDomain @Inject constructor(
    private val tenantService: TenantService,
    private val tenantDomainService: TenantDomainService,
    private val tenantMapper: TenantMapper,
) : EndpointHandler<TenantApi.GetByDomain, TenantRep.Complete>(
    template = TenantApi.GetByDomain::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TenantApi.GetByDomain =
      TenantApi.GetByDomain(domain = call.getParam(String::class, "domain"))

  override suspend fun Handler.handle(endpoint: TenantApi.GetByDomain): TenantRep.Complete {
    auth(Auth.Allow)
    val tenant = tenantService.getByDomain(endpoint.domain) ?: throw TenantNotFound()
    val domains = tenantDomainService.getByOrgGuid(tenant.orgGuid)
    return tenantMapper.completeRep(tenant, domains)
  }
}
