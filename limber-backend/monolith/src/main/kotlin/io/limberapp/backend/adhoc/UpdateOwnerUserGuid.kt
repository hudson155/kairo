package io.limberapp.backend.adhoc

import com.google.inject.Injector
import io.ktor.application.Application
import io.limberapp.backend.BaseLimberApp
import io.limberapp.backend.LimberModule
import io.limberapp.backend.config.LimberConfigLoader
import io.limberapp.backend.module.LimberSqlModule
import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.common.shutDown
import java.util.*

private object UpdateOwnerUserGuidArgs {
  val orgGuid: UUID get() = TODO()
  val ownerUserGuid: UUID get() = TODO()
}

internal fun Adhoc.updateOwnerUserGuid() {
  val config = LimberConfigLoader().load()

  object : BaseLimberApp(application, config) {
    override fun getMainModules(application: Application) =
      super.getMainModules(application) + LimberSqlModule(config.sqlDatabase, runMigrations = false)

    override val modules = allLimberModules()

    override fun Application.afterStart(context: Context) {
      updateOwnerUserGuid(context.injector)
      shutDown(0)
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
