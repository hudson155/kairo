package io.limberapp.backend.module.auth.endpoint.tenant

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.exception.tenant.TenantNotFound
import io.limberapp.backend.module.auth.mapper.tenant.TenantMapper
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.common.restInterface.template

internal class GetTenantByDomain @Inject constructor(
    application: Application,
    private val tenantService: TenantService,
    private val tenantDomainService: TenantDomainService,
    private val tenantMapper: TenantMapper,
) : LimberApiEndpoint<TenantApi.GetByDomain, TenantRep.Complete>(
    application = application,
    endpointTemplate = TenantApi.GetByDomain::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = TenantApi.GetByDomain(
      domain = call.parameters.getAsType(String::class, "domain")
  )

  override suspend fun Handler.handle(command: TenantApi.GetByDomain): TenantRep.Complete {
    Authorization.Public.authorize()
    val tenant = tenantService.getByDomain(command.domain) ?: throw TenantNotFound()
    val domains = tenantDomainService.getByOrgGuid(tenant.orgGuid)
    return tenantMapper.completeRep(tenant, domains)
  }
}
