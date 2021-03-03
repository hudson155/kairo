package io.limberapp.module.auth

import io.limberapp.endpoint.feature.DeleteFeatureRole
import io.limberapp.endpoint.feature.GetFeatureRolesByFeatureGuid
import io.limberapp.endpoint.feature.PatchFeatureRole
import io.limberapp.endpoint.feature.PostFeatureRole
import io.limberapp.endpoint.org.DeleteOrgRole
import io.limberapp.endpoint.org.DeleteOrgRoleMembership
import io.limberapp.endpoint.org.GetOrgRoleMembershipsByOrgRoleGuid
import io.limberapp.endpoint.org.GetOrgRolesByOrgGuid
import io.limberapp.endpoint.org.PatchOrgRole
import io.limberapp.endpoint.org.PostOrgRole
import io.limberapp.endpoint.org.PostOrgRoleMembership
import io.limberapp.endpoint.tenant.DeleteTenant
import io.limberapp.endpoint.tenant.DeleteTenantDomain
import io.limberapp.endpoint.tenant.GetTenant
import io.limberapp.endpoint.tenant.GetTenantByDomain
import io.limberapp.endpoint.tenant.PatchTenant
import io.limberapp.endpoint.tenant.PostTenant
import io.limberapp.endpoint.tenant.PostTenantDomain
import io.limberapp.module.Feature
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.service.feature.FeatureRoleService
import io.limberapp.service.feature.FeatureRoleServiceImpl
import io.limberapp.service.org.OrgRoleMembershipService
import io.limberapp.service.org.OrgRoleMembershipServiceImpl
import io.limberapp.service.org.OrgRoleService
import io.limberapp.service.org.OrgRoleServiceImpl
import io.limberapp.service.tenant.TenantDomainService
import io.limberapp.service.tenant.TenantDomainServiceImpl
import io.limberapp.service.tenant.TenantService
import io.limberapp.service.tenant.TenantServiceImpl
import kotlin.reflect.KClass

class AuthFeature : Feature() {
  override val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> = listOf(
      PostFeatureRole::class,
      GetFeatureRolesByFeatureGuid::class,
      PatchFeatureRole::class,
      DeleteFeatureRole::class,

      PostOrgRole::class,
      GetOrgRolesByOrgGuid::class,
      PatchOrgRole::class,
      DeleteOrgRole::class,
      PostOrgRoleMembership::class,
      GetOrgRoleMembershipsByOrgRoleGuid::class,
      DeleteOrgRoleMembership::class,

      PostTenant::class,
      GetTenant::class,
      GetTenantByDomain::class,
      PatchTenant::class,
      DeleteTenant::class,
      PostTenantDomain::class,
      DeleteTenantDomain::class,
  )

  override fun bind() {
    bind(FeatureRoleService::class.java).to(FeatureRoleServiceImpl::class.java)
        .asEagerSingleton()

    bind(OrgRoleService::class.java).to(OrgRoleServiceImpl::class.java)
        .asEagerSingleton()
    bind(OrgRoleMembershipService::class.java).to(OrgRoleMembershipServiceImpl::class.java)
        .asEagerSingleton()

    bind(TenantService::class.java).to(TenantServiceImpl::class.java)
        .asEagerSingleton()
    bind(TenantDomainService::class.java).to(TenantDomainServiceImpl::class.java)
        .asEagerSingleton()
  }

  override fun cleanUp(): Unit = Unit
}
