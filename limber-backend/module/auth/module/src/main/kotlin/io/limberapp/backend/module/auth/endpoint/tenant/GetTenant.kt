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
import java.util.*

internal class GetTenant @Inject constructor(
    application: Application,
    private val tenantService: TenantService,
    private val tenantDomainService: TenantDomainService,
    private val tenantMapper: TenantMapper,
) : LimberApiEndpoint<TenantApi.Get, TenantRep.Complete>(
    application = application,
    endpointTemplate = TenantApi.Get::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = TenantApi.Get(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid")
  )

  override suspend fun Handler.handle(command: TenantApi.Get): TenantRep.Complete {
    Authorization.Public.authorize()
    val tenant = tenantService.get(command.orgGuid) ?: throw TenantNotFound()
    val domains = tenantDomainService.getByOrgGuid(command.orgGuid)
    return tenantMapper.completeRep(tenant, domains)
  }
}
