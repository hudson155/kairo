package io.limberapp.backend.test

import com.google.inject.Injector
import com.piperframework.ktorAuth.piperAuth
import com.piperframework.module.Module
import com.piperframework.module.ModuleWithLifecycle
import com.piperframework.testing.TestPiperApp
import com.piperframework.util.uuid.UuidGenerator
import io.ktor.application.Application
import io.ktor.auth.Authentication
import io.limberapp.backend.authentication.jwt.JwtAuthVerifier
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.config.LimberAppMonolithConfig
import java.time.Clock

class TestLimberApp(
  application: Application,
  config: LimberAppMonolithConfig,
  module: Module,
  additionalModules: Set<ModuleWithLifecycle>,
  fixedClock: Clock,
  deterministicUuidGenerator: UuidGenerator,
) : TestPiperApp(application, config, module, additionalModules, fixedClock, deterministicUuidGenerator) {
  override fun Authentication.Configuration.configureAuthentication(injector: Injector) {
    piperAuth<Jwt> {
      verifier(JwtAuthVerifier.scheme, JwtAuthVerifier(config.authentication), default = true)
    }
  }
}
