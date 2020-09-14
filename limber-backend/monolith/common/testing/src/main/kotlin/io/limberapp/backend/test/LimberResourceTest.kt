package io.limberapp.backend.test

import io.limberapp.backend.config.LimberConfigLoader
import io.limberapp.common.module.Module
import io.limberapp.common.module.ModuleWithLifecycle
import io.limberapp.common.serialization.Json
import io.limberapp.common.testing.AbstractResourceTest

abstract class LimberResourceTest : AbstractResourceTest() {
  protected val config = LimberConfigLoader().load("test")

  protected val json by lazy { Json(serializersModule = module.serializersModule) }

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
