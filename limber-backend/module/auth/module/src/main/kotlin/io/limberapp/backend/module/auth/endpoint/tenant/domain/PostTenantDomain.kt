package io.limberapp.backend.module.auth.endpoint.tenant.domain

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.authorization.AuthAccountRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.tenant.TenantDomainApi
import io.limberapp.backend.module.auth.mapper.tenant.TenantDomainMapper
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import io.limberapp.common.permissions.AccountRole
import io.limberapp.common.restInterface.template
import java.util.*

internal class PostTenantDomain @Inject constructor(
    application: Application,
    private val tenantDomainService: TenantDomainService,
    private val tenantDomainMapper: TenantDomainMapper,
) : LimberApiEndpoint<TenantDomainApi.Post, TenantDomainRep.Complete>(
    application = application,
    endpointTemplate = TenantDomainApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = TenantDomainApi.Post(
      orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
      rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: TenantDomainApi.Post): TenantDomainRep.Complete {
    val rep = command.rep.required()
    auth { AuthAccountRole(AccountRole.SUPERUSER) }
    val tenantDomain = tenantDomainService.create(tenantDomainMapper.model(command.orgGuid, rep))
    return tenantDomainMapper.completeRep(tenantDomain)
  }
}
