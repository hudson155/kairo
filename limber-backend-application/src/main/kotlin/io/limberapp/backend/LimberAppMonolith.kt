package io.limberapp.backend

import com.google.inject.Injector
import com.piperframework.SimplePiperApp
import com.piperframework.ktorAuth.piperAuth
import com.piperframework.module.MainModule
import io.ktor.application.Application
import io.ktor.auth.Authentication
import io.limberapp.backend.authentication.jwt.JwtAuthVerifier
import io.limberapp.backend.authentication.token.TokenAuthVerifier
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.config.LimberAppMonolithConfig
import io.limberapp.backend.config.LimberConfigLoader
import io.limberapp.backend.module.LimberSqlModule
import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenService
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import io.limberapp.backend.module.forms.FormsModule
import io.limberapp.backend.module.healthCheck.HealthCheckModule
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.module.users.UsersModule

internal class LimberAppMonolith(application: Application) : SimplePiperApp<LimberAppMonolithConfig>(
    application = application,
    config = LimberConfigLoader().load()
) {
    override fun Authentication.Configuration.configureAuthentication(injector: Injector) {
        piperAuth<Jwt> {
            verifier(
                scheme = JwtAuthVerifier.scheme,
                verifier = JwtAuthVerifier(config.authentication),
                default = true
            )
            verifier(
                scheme = TokenAuthVerifier.scheme,
                verifier = TokenAuthVerifier(
                    injector.getInstance(JwtClaimsRequestService::class.java),
                    injector.getInstance(AccessTokenService::class.java)
                )
            )
        }
    }

    override fun getMainModules(application: Application) = listOf(
        MainModule.forProduction(application, config),
        LimberSqlModule(config.sqlDatabase)
    )

    override val modules = listOf(
        HealthCheckModule(),

        AuthModule(),
        FormsModule(),
        OrgsModule(),
        UsersModule()
    )
}
