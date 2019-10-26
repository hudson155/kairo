package io.limberapp.backend

import com.auth0.jwk.UrlJwkProvider
import com.google.inject.Guice
import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.jackson.JacksonConverter
import io.ktor.server.cio.EngineMain
import io.limberapp.backend.config.Config
import io.limberapp.backend.config.database.DatabaseConfig
import io.limberapp.backend.config.jwt.JwtConfig
import io.limberapp.backend.module.orgs.OrgsModule
import io.limberapp.framework.dataConversion.conversionService.GuidConversionService
import io.limberapp.framework.exceptionMapping.ExceptionMappingConfigurator
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import org.slf4j.event.Level
import java.util.UUID

/**
 * Main entry point for the entire application.
 */
internal fun main(args: Array<String>) = EngineMain.main(args)

/**
 * Application configuration method, used automatically by Ktor to configure and set up the
 * application.
 */
internal fun Application.main() {

    val config = with(ConfigFactory.load()) {
        Config(
            database = DatabaseConfig(
                host = getString("database.host"),
                database = getString("database.database"),
                user = getString("database.user"),
                password = getString("database.password")
            ),
            jwt = JwtConfig(
                domain = getString("jwt.domain")
            )
        )
    }

    install(Authentication) {
        jwt {
            verifier(UrlJwkProvider(config.jwt.domain))
            validate { credential -> JWTPrincipal(credential.payload) }
        }
    }
    install(CORS) {
        anyHost()
    }
    install(DataConversion) {
        convert(UUID::class, GuidConversionService())
    }
    install(DefaultHeaders)
    install(Compression)
    install(CallLogging) {
        level = Level.INFO
    }
    install(ContentNegotiation) {
        register(
            ContentType.Application.Json,
            JacksonConverter(LimberObjectMapper(prettyPrint = true))
        )
    }
    install(StatusPages) {
        ExceptionMappingConfigurator().configureExceptionMapping(this)
    }

    Guice.createInjector(
        MainModule(this, config),
        OrgsModule()
    )
}
