package io.limberapp.backend.test

import io.limberapp.common.module.ApplicationModule
import io.limberapp.common.module.GuiceModule
import io.limberapp.common.serialization.Json
import io.limberapp.common.testing.AbstractResourceTest
import io.limberapp.config.ConfigLoader
import io.limberapp.monolith.config.LimberMonolithConfig

abstract class LimberResourceTest : AbstractResourceTest() {
  protected val config = ConfigLoader.load<LimberMonolithConfig>("test")

  protected val json = Json()

  protected abstract val module: ApplicationModule

  protected abstract val additionalModules: Set<GuiceModule>

  final override val limberTest by lazy {
    LimberTest(json) {
      TestLimberApp(
        application = this,
        config = config,
        module = module,
        additionalModules = additionalModules,
      )
    }
  }
}
