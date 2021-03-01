package io.limberapp.endpoint.tenant

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.tenant.TenantApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.mapper.tenant.TenantMapper
import io.limberapp.rep.tenant.TenantRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.tenant.TenantDomainService
import io.limberapp.service.tenant.TenantService
import java.util.UUID

internal class PatchTenant @Inject constructor(
    private val tenantService: TenantService,
    private val tenantDomainService: TenantDomainService,
    private val tenantMapper: TenantMapper,
) : EndpointHandler<TenantApi.Patch, TenantRep.Complete>(
    template = TenantApi.Patch::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TenantApi.Patch =
      TenantApi.Patch(
          orgGuid = call.getParam(UUID::class, "orgGuid"),
          rep = call.getAndValidateBody(),
      )

  override suspend fun Handler.handle(endpoint: TenantApi.Patch): TenantRep.Complete {
    val rep = endpoint.rep.required()
    auth(AuthSuperuser)
    val tenant = tenantService.update(endpoint.orgGuid, tenantMapper.update(rep))
    val domains = tenantDomainService.getByOrgGuid(endpoint.orgGuid)
    return tenantMapper.completeRep(tenant, domains)
  }
}
