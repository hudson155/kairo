package io.limberapp.endpoint.tenant

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.tenant.TenantApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.mapper.tenant.TenantMapper
import io.limberapp.rep.tenant.TenantRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.tenant.TenantService

internal class PostTenant @Inject constructor(
    private val tenantService: TenantService,
    private val tenantMapper: TenantMapper,
) : EndpointHandler<TenantApi.Post, TenantRep.Complete>(
    template = TenantApi.Post::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): TenantApi.Post =
      TenantApi.Post(rep = call.getAndValidateBody())

  override suspend fun Handler.handle(endpoint: TenantApi.Post): TenantRep.Complete {
    val rep = endpoint.rep.required()
    auth(AuthSuperuser)
    val tenant = tenantService.create(tenantMapper.model(rep))
    return tenantMapper.completeRep(tenant, emptySet())
  }
}
