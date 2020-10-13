package io.limberapp.monolith

import io.ktor.application.Application
import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.backend.module.forms.FormsModule
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.module.users.UsersModule
import io.limberapp.common.LimberApplication
import io.limberapp.monolith.config.LimberMonolithConfig

internal abstract class BaseLimberApp(
    application: Application,
    config: LimberMonolithConfig,
) : LimberApplication<LimberMonolithConfig>(application, config) {
  protected fun allLimberModules() = listOf(
      AuthModule(),
      FormsModule(),
      OrgsModule(),
      UsersModule(),
  )
}
