package io.limberapp.common.testing

import io.ktor.application.Application
import io.limberapp.common.SimpleLimberApp
import io.limberapp.common.config.Config
import io.limberapp.common.module.MainModule
import io.limberapp.common.module.Module
import io.limberapp.common.module.ModuleWithLifecycle
import io.limberapp.common.util.uuid.UuidGenerator
import java.time.Clock

abstract class TestLimberApp(
  application: Application,
  config: Config,
  module: Module,
  private val additionalModules: Set<ModuleWithLifecycle>,
  private val fixedClock: Clock,
  private val deterministicUuidGenerator: UuidGenerator,
) : SimpleLimberApp<Config>(application, config) {
  final override fun getMainModules(application: Application) =
    listOf(MainModule(application, fixedClock, config, deterministicUuidGenerator)) + additionalModules

  final override val modules = listOf(module)
}
