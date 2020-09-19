package io.limberapp.common.testing

import io.ktor.application.Application
import io.limberapp.common.LimberApplication
import io.limberapp.common.module.MainModule
import io.limberapp.common.module.Module
import io.limberapp.common.module.ModuleWithLifecycle
import io.limberapp.common.util.uuid.UuidGenerator
import io.limberapp.config.Config
import java.time.Clock

abstract class TestLimberApp(
  application: Application,
  protected val config: Config,
  module: Module,
  private val additionalModules: Set<ModuleWithLifecycle>,
  private val fixedClock: Clock,
  private val deterministicUuidGenerator: UuidGenerator,
) : LimberApplication(application) {
  final override fun getMainModules(application: Application) =
    listOf(MainModule(application, fixedClock, config, deterministicUuidGenerator)) + additionalModules

  final override val modules = listOf(module)
}
