package io.limberapp.monolith

import io.ktor.application.Application
import io.limberapp.backend.module.LimberSqlModule
import io.limberapp.common.module.healthCheck.HealthCheckModule
import io.limberapp.config.ConfigLoader
import io.limberapp.module.monolith.MonolithModule
import io.limberapp.monolith.config.LimberAppMonolithConfig

internal class LimberAppMonolith(application: Application) : BaseLimberApp(
  application = application,
  config = ConfigLoader.load(System.getenv("LIMBER_CONFIG"), LimberAppMonolithConfig::class),
) {
  override fun getMainModules(application: Application) =
    super.getMainModules(application) + LimberSqlModule(config.sqlDatabase, runMigrations = true)

  override val modules = listOf(MonolithModule(), HealthCheckModule()) + allLimberModules()
}
