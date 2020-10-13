package io.limberapp.monolith.adhoc

import com.google.inject.Injector
import io.ktor.application.Application
import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.LimberSqlModule
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.common.shutDown
import io.limberapp.config.ConfigLoader
import io.limberapp.monolith.BaseLimberApp
import io.limberapp.monolith.config.LimberMonolithConfig
import java.util.*

private object UpdateOwnerUserGuidArgs {
  val orgGuid: UUID get() = TODO()
  val ownerUserGuid: UUID get() = TODO()
}

internal fun Adhoc.updateOwnerUserGuid() {
  val config = ConfigLoader.load<LimberMonolithConfig>(System.getenv("LIMBER_CONFIG"))

  object : BaseLimberApp(application, config) {
    override fun getApplicationModules() = allLimberModules()

    override fun getAdditionalModules() = listOf(LimberSqlModule(config.sqlDatabase, runMigrations = false))

    override fun afterStart(application: Application, injector: Injector) {
      updateOwnerUserGuid(injector)
      application.shutDown(0)
    }

    @OptIn(LimberModule.Orgs::class)
    private fun updateOwnerUserGuid(injector: Injector): UUID {
      val orgService = injector.getInstance(OrgService::class.java)
      return orgService.update(UpdateOwnerUserGuidArgs.orgGuid, OrgModel.Update(
          name = null,
          ownerUserGuid = UpdateOwnerUserGuidArgs.ownerUserGuid,
      )).guid
    }
  }
}
