@file:Suppress("UNREACHABLE_CODE")

package io.limberapp.monolith.adhoc

import com.google.inject.Injector
import io.ktor.application.Application
import io.limberapp.backend.module.SqlModule
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import io.limberapp.backend.module.auth.model.tenant.TenantDomainModel
import io.limberapp.backend.module.auth.model.tenant.TenantModel
import io.limberapp.backend.module.auth.service.org.OrgRoleService
import io.limberapp.backend.module.auth.service.tenant.TenantDomainService
import io.limberapp.backend.module.auth.service.tenant.TenantService
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.common.config.ConfigLoader
import io.limberapp.common.permissions.orgPermissions.OrgPermission
import io.limberapp.common.permissions.orgPermissions.OrgPermissions
import io.limberapp.common.shutDown
import io.limberapp.monolith.BaseLimberApp
import io.limberapp.monolith.config.LimberMonolithConfig
import java.time.ZonedDateTime
import java.util.*

private object OnboardArgs {
  val orgName: String get() = TODO()
  val auth0ClientId: String get() = TODO()
  val orgDomain: String get() = TODO()
}

internal fun Adhoc.onboard() {
  val config = ConfigLoader.load<LimberMonolithConfig>(System.getenv("LIMBER_CONFIG"))

  object : BaseLimberApp(application, config) {
    override fun getApplicationModules() = allLimberModules()

    override fun getAdditionalModules() = listOf(SqlModule(config.sqlDatabase, runMigrations = false))

    override fun afterStart(application: Application, injector: Injector) {
      val orgGuid = createOrg(injector)
      createTenant(injector, orgGuid = orgGuid)
      createTenantDomain(injector, orgGuid = orgGuid)
      createOrgRoles(injector, orgGuid = orgGuid)
      application.shutDown(0)
    }

    private fun createOrg(injector: Injector): UUID {
      val orgService = injector.getInstance(OrgService::class.java)
      return orgService.create(OrgModel(
          guid = UUID.randomUUID(),
          createdDate = ZonedDateTime.now(),
          name = OnboardArgs.orgName,
          ownerUserGuid = null,
      )).guid
    }

    private fun createTenant(injector: Injector, orgGuid: UUID) {
      val tenantService = injector.getInstance(TenantService::class.java)
      tenantService.create(TenantModel(
          createdDate = ZonedDateTime.now(),
          orgGuid = orgGuid,
          name = OnboardArgs.orgName,
          auth0ClientId = OnboardArgs.auth0ClientId,
      ))
    }

    private fun createTenantDomain(injector: Injector, orgGuid: UUID) {
      val tenantDomainService = injector.getInstance(TenantDomainService::class.java)
      tenantDomainService.create(TenantDomainModel(
          createdDate = ZonedDateTime.now(),
          orgGuid = orgGuid,
          domain = OnboardArgs.orgDomain,
      ))
    }

    private fun createOrgRoles(injector: Injector, orgGuid: UUID) {
      val orgRoleService = injector.getInstance(OrgRoleService::class.java)
      orgRoleService.create(OrgRoleModel(
          guid = UUID.randomUUID(),
          createdDate = ZonedDateTime.now(),
          orgGuid = orgGuid,
          name = "Members",
          permissions = OrgPermissions(setOf(OrgPermission.MODIFY_OWN_METADATA)),
          isDefault = true,
          memberCount = 0,
      ))
      orgRoleService.create(OrgRoleModel(
          guid = UUID.randomUUID(),
          createdDate = ZonedDateTime.now(),
          orgGuid = orgGuid,
          name = "Managers",
          permissions = OrgPermissions.none(),
          isDefault = false,
          memberCount = 0,
      ))
    }
  }
}
