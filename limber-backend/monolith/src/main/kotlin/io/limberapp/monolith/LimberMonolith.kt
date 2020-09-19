package io.limberapp.monolith

import com.google.inject.Injector
import io.ktor.application.Application
import io.limberapp.backend.module.LimberSqlModule
import io.limberapp.common.module.healthCheck.HealthCheckModule
import io.limberapp.config.ConfigLoader
import io.limberapp.module.monolith.MonolithModule
import io.limberapp.monolith.config.LimberMonolithConfig

internal class LimberMonolith(application: Application) : BaseLimberApp(
  application = application,
  config = ConfigLoader.load(System.getenv("LIMBER_CONFIG"), LimberMonolithConfig::class),
) {
  override fun getMainModules(application: Application) =
    super.getMainModules(application) + LimberSqlModule(config.sqlDatabase, runMigrations = true)

  override val modules = listOf(MonolithModule(), HealthCheckModule()) + allLimberModules()

  override fun afterStart(application: Application, injector: Injector) = Unit
}
