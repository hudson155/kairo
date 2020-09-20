package io.limberapp.monolith

import io.ktor.application.Application
import io.limberapp.backend.module.LimberSqlModule
import io.limberapp.common.module.healthCheck.HealthCheckModule
import io.limberapp.config.ConfigLoader
import io.limberapp.module.monolith.MonolithModule

internal class LimberMonolith(application: Application) : BaseLimberApp(
  application = application,
  config = ConfigLoader.load(System.getenv("LIMBER_CONFIG")),
) {
  override fun getApplicationModules() = listOf(MonolithModule(), HealthCheckModule()) + allLimberModules()

  override fun getAdditionalModules() = listOf(LimberSqlModule(config.sqlDatabase, runMigrations = true))
}
