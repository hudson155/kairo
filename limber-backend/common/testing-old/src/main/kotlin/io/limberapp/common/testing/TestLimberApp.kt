package io.limberapp.common.testing

import io.ktor.application.Application
import io.limberapp.common.LimberApplication
import io.limberapp.common.module.ApplicationModule
import io.limberapp.common.module.GuiceModule
import io.limberapp.config.Config

abstract class TestLimberApp(
  application: Application,
  config: Config,
  private val module: ApplicationModule,
  private val additionalModules: Set<GuiceModule>,
) : LimberApplication<Config>(application, config) {
  final override fun getApplicationModules() = listOf(module)

  final override fun getAdditionalModules() = additionalModules.toList()
}
