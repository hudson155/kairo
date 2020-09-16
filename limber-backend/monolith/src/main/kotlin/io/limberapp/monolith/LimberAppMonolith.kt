package io.limberapp.monolith

import io.ktor.application.Application
import io.limberapp.backend.module.LimberSqlModule
import io.limberapp.common.module.healthCheck.HealthCheckModule
import io.limberapp.monolith.config.LimberConfigLoader
import io.limberapp.module.monolith.MonolithModule

internal class LimberAppMonolith(application: Application) : BaseLimberApp(
  application = application,
  config = LimberConfigLoader().load(),
) {
  override fun getMainModules(application: Application) =
    super.getMainModules(application) + LimberSqlModule(config.sqlDatabase, runMigrations = true)

  override val modules = listOf(MonolithModule(), HealthCheckModule()) + allLimberModules()
}
