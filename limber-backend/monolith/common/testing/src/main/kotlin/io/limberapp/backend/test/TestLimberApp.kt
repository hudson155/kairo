package io.limberapp.backend.test

import com.google.inject.Injector
import io.ktor.application.Application
import io.ktor.auth.Authentication
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.common.ktorAuth.limberAuth
import io.limberapp.common.module.Module
import io.limberapp.common.module.ModuleWithLifecycle
import io.limberapp.common.testing.TestLimberApp
import io.limberapp.common.util.uuid.UuidGenerator
import io.limberapp.monolith.authentication.jwt.JwtAuthVerifier
import io.limberapp.monolith.config.LimberMonolithConfig
import java.time.Clock

class TestLimberApp(
  application: Application,
  config: LimberMonolithConfig,
  module: Module,
  additionalModules: Set<ModuleWithLifecycle>,
  fixedClock: Clock,
  deterministicUuidGenerator: UuidGenerator,
) : TestLimberApp(application, config, module, additionalModules, fixedClock, deterministicUuidGenerator) {
  override fun Authentication.Configuration.configureAuthentication(injector: Injector) {
    limberAuth<Jwt> {
      verifier(JwtAuthVerifier.scheme, JwtAuthVerifier(config.authentication), default = true)
    }
  }

  override fun afterStart(application: Application, injector: Injector) = Unit
}
