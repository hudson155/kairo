package io.limberapp.backend.module.auth.endpoint.tenant

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.mapper.tenant.TenantMapper
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.common.restInterface.template
import java.util.*

internal class PatchTenant @Inject constructor(
  application: Application,
  private val tenantService: TenantService,
  private val tenantDomainService: TenantDomainService,
  private val tenantMapper: TenantMapper,
) : LimberApiEndpoint<TenantApi.Patch, TenantRep.Complete>(
  application = application,
  endpointTemplate = TenantApi.Patch::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = TenantApi.Patch(
    orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: TenantApi.Patch): TenantRep.Complete {
    val rep = command.rep.required()
    Authorization.Role(JwtRole.SUPERUSER).authorize()
    val tenant = tenantService.update(command.orgGuid, tenantMapper.update(rep))
    val domains = tenantDomainService.findAsSet { orgGuid(tenant.orgGuid) }
    return tenantMapper.completeRep(tenant, domains)
  }
}
