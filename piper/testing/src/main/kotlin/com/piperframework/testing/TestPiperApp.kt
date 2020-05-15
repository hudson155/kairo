package com.piperframework.testing

import com.piperframework.SimplePiperApp
import com.piperframework.config.Config
import com.piperframework.module.MainModule
import com.piperframework.module.Module
import com.piperframework.module.ModuleWithLifecycle
import com.piperframework.util.uuid.UuidGenerator
import io.ktor.application.Application
import java.time.Clock

abstract class TestPiperApp(
  application: Application,
  config: Config,
  module: Module,
  private val additionalModules: Set<ModuleWithLifecycle>,
  private val fixedClock: Clock,
  private val deterministicUuidGenerator: UuidGenerator
) : SimplePiperApp<Config>(application, config) {
  final override fun getMainModules(application: Application) =
    listOf(MainModule(application, fixedClock, config, deterministicUuidGenerator)).plus(additionalModules)

  final override val modules = listOf(module)
}
