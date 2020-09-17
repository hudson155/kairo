package io.limberapp.backend.test

import io.limberapp.common.module.Module
import io.limberapp.common.module.ModuleWithLifecycle
import io.limberapp.common.serialization.Json
import io.limberapp.common.testing.AbstractResourceTest
import io.limberapp.config.ConfigLoader
import io.limberapp.monolith.config.LimberAppMonolithConfig

abstract class LimberResourceTest : AbstractResourceTest() {
  protected val config = ConfigLoader.load("test", LimberAppMonolithConfig::class)

  protected val json = Json()

  protected abstract val module: Module

  protected abstract val additionalModules: Set<ModuleWithLifecycle>

  final override val limberTest by lazy {
    LimberTest(json) {
      TestLimberApp(
        application = this,
        config = config,
        module = module,
        additionalModules = additionalModules,
        fixedClock = fixedClock,
        deterministicUuidGenerator = deterministicUuidGenerator
      )
    }
  }
}
