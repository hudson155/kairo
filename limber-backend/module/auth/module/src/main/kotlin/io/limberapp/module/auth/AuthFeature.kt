package io.limberapp.module.auth

import io.limberapp.endpoint.tenant.DeleteTenant
import io.limberapp.endpoint.tenant.DeleteTenantDomain
import io.limberapp.endpoint.tenant.GetTenant
import io.limberapp.endpoint.tenant.GetTenantByDomain
import io.limberapp.endpoint.tenant.PatchTenant
import io.limberapp.endpoint.tenant.PostTenant
import io.limberapp.endpoint.tenant.PostTenantDomain
import io.limberapp.module.Feature
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.service.tenant.TenantDomainService
import io.limberapp.service.tenant.TenantDomainServiceImpl
import io.limberapp.service.tenant.TenantService
import io.limberapp.service.tenant.TenantServiceImpl
import kotlin.reflect.KClass

class AuthFeature : Feature() {
  override val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> = listOf(
      PostTenant::class,
      GetTenant::class,
      GetTenantByDomain::class,
      PatchTenant::class,
      DeleteTenant::class,
      PostTenantDomain::class,
      DeleteTenantDomain::class
  )

  override fun bind() {
    bind(TenantService::class.java).to(TenantServiceImpl::class.java)
        .asEagerSingleton()
    bind(TenantDomainService::class.java).to(TenantDomainServiceImpl::class.java)
        .asEagerSingleton()
  }

  override fun cleanUp(): Unit = Unit
}
