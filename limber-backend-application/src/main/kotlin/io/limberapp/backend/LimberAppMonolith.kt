package io.limberapp.backend

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Injector
import com.piperframework.PiperApp
import com.piperframework.jackson.objectMapper.PiperObjectMapper
import com.piperframework.ktorAuth.piperAuth
import com.piperframework.module.MainModule
import com.piperframework.module.MongoModule
import io.ktor.application.Application
import io.ktor.auth.Authentication
import io.limberapp.backend.authentication.jwt.JwtAuthVerifier
import io.limberapp.backend.authentication.token.TokenAuthVerifier
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.module.auth.AuthModule
import io.limberapp.backend.module.auth.service.jwtClaimsRequest.JwtClaimsRequestService
import io.limberapp.backend.module.auth.service.personalAccessToken.PersonalAccessTokenService
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.backend.module.users.UsersModule

internal class LimberAppMonolith : PiperApp<Config>(loadConfig()) {

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
                    injector.getInstance(PersonalAccessTokenService::class.java)
                )
            )
        }
    }

    override fun getMainModules(application: Application) = listOf(
        MainModule.forProduction(application, config),
        MongoModule(config.mongoDatabase)
    )

    override val modules = listOf(
        AuthModule(),
        OrgsModule(),
        UsersModule()
    )
}

private val yamlObjectMapper = PiperObjectMapper(YAMLFactory())

private fun loadConfig(): Config {
    val envString = System.getenv("LIMBERAPP_ENV") ?: "prod"
    val stream = object {}.javaClass.getResourceAsStream("/config/$envString.yml")
        ?: error("Config for LIMBER_ENV=$envString not found.")
    return yamlObjectMapper.readValue(stream)
}
