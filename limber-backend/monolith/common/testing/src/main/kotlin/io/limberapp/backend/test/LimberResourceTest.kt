package io.limberapp.backend.test

import com.piperframework.module.Module
import com.piperframework.module.ModuleWithLifecycle
import com.piperframework.serialization.Json
import com.piperframework.testing.AbstractResourceTest
import io.limberapp.backend.config.LimberConfigLoader

abstract class LimberResourceTest : AbstractResourceTest() {
  protected val config = LimberConfigLoader().load("test")

  protected val json by lazy { Json(serializersModule = module.serializersModule) }

  protected abstract val module: Module

  protected abstract val additionalModules: Set<ModuleWithLifecycle>

  final override val piperTest by lazy {
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
