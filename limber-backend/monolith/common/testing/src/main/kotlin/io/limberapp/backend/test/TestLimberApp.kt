package io.limberapp.backend.test

import io.ktor.application.Application
import io.limberapp.common.module.ApplicationModule
import io.limberapp.common.module.GuiceModule
import io.limberapp.common.testing.TestLimberApp
import io.limberapp.monolith.config.LimberMonolithConfig

class TestLimberApp(
  application: Application,
  config: LimberMonolithConfig,
  module: ApplicationModule,
  additionalModules: Set<GuiceModule>,
) : TestLimberApp(application, config, module, additionalModules)
