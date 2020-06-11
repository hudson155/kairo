package io.limberapp.backend.module.auth.endpoint.tenant.domain

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.tenant.domain.TenantDomainApi
import io.limberapp.backend.module.auth.mapper.tenant.TenantDomainMapper
import io.limberapp.backend.module.auth.rep.tenant.TenantDomainRep
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import java.util.*

/**
 * Creates a new domain within a tenant.
 */
internal class PostTenantDomain @Inject constructor(
  application: Application,
  servingConfig: ServingConfig,
  private val tenantDomainService: TenantDomainService,
  private val tenantDomainMapper: TenantDomainMapper
) : LimberApiEndpoint<TenantDomainApi.Post, TenantDomainRep.Complete>(
  application, servingConfig.apiPathPrefix,
  endpointTemplate = TenantDomainApi.Post::class.template()
) {
  override suspend fun determineCommand(call: ApplicationCall) = TenantDomainApi.Post(
    orgGuid = call.parameters.getAsType(UUID::class, "orgGuid"),
    rep = call.getAndValidateBody()
  )

  override suspend fun Handler.handle(command: TenantDomainApi.Post): TenantDomainRep.Complete {
    val rep = command.rep.required()
    Authorization.Role(JwtRole.SUPERUSER).authorize()
    val tenantDomain = tenantDomainService.create(tenantDomainMapper.model(command.orgGuid, rep))
    return tenantDomainMapper.completeRep(tenantDomain)
  }
}
