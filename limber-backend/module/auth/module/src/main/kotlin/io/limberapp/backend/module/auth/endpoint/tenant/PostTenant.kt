package io.limberapp.backend.module.auth.endpoint.tenant

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.mapper.tenant.TenantDomainMapper
import io.limberapp.backend.module.auth.mapper.tenant.TenantMapper
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.common.restInterface.template
import io.limberapp.permissions.AccountRole

internal class PostTenant @Inject constructor(
    application: Application,
    private val tenantService: TenantService,
    private val tenantDomainService: TenantDomainService,
    private val tenantMapper: TenantMapper,
    private val tenantDomainMapper: TenantDomainMapper,
) : LimberApiEndpoint<TenantApi.Post, TenantRep.Complete>(
    application = application,
    endpointTemplate = TenantApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = TenantApi.Post(
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: TenantApi.Post): TenantRep.Complete {
    val rep = command.rep.required()
    Authorization.Role(AccountRole.SUPERUSER).authorize()
    val tenant = tenantService.create(tenantMapper.model(rep))
    val domain = tenantDomainService.create(tenantDomainMapper.model(tenant.orgGuid, rep.domain))
    return tenantMapper.completeRep(tenant, setOf(domain))
  }
}
