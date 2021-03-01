package io.limberapp.endpoint.tenant

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.tenant.TenantDomainApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.mapper.tenant.TenantDomainMapper
import io.limberapp.rep.tenant.TenantDomainRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.tenant.TenantDomainService
import java.util.UUID

internal class PostTenantDomain @Inject constructor(
    private val tenantDomainService: TenantDomainService,
    private val tenantDomainMapper: TenantDomainMapper,
) : EndpointHandler<TenantDomainApi.Post, TenantDomainRep.Complete>(
    template = TenantDomainApi.Post::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TenantDomainApi.Post =
      TenantDomainApi.Post(
          orgGuid = call.getParam(UUID::class, "orgGuid"),
          rep = call.getAndValidateBody(),
      )

  override suspend fun Handler.handle(endpoint: TenantDomainApi.Post): TenantDomainRep.Complete {
    val rep = endpoint.rep.required()
    auth(AuthSuperuser)
    val tenantDomain = tenantDomainService.create(tenantDomainMapper.model(endpoint.orgGuid, rep))
    return tenantDomainMapper.completeRep(tenantDomain)
  }
}
