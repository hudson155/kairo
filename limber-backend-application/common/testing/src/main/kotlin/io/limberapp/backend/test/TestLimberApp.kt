package io.limberapp.backend.test

import com.google.inject.AbstractModule
import com.piperframework.config.Config
import com.piperframework.ktorAuth.piperAuth
import com.piperframework.module.Module
import com.piperframework.testing.TestPiperApp
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.limberapp.backend.authentication.JwtAuthVerifier
import io.limberapp.backend.authorization.principal.Jwt
import java.time.Clock

class TestLimberApp(
    config: Config,
    module: Module,
    additionalModules: List<AbstractModule>,
    fixedClock: Clock,
    deterministicUuidGenerator: UuidGenerator
) : TestPiperApp(config, module, additionalModules, fixedClock, deterministicUuidGenerator) {

    override fun Application.authentication() {
        install(Authentication) {
            piperAuth<Jwt> {
                verifier(JwtAuthVerifier.scheme, JwtAuthVerifier(config.authentication), default = true)
            }
        }
    }
}
