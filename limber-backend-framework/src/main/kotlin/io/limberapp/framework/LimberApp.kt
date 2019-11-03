package io.limberapp.framework

import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.inject.Guice
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
import io.limberapp.framework.config.Config
import io.limberapp.framework.dataConversion.conversionService.UuidConversionService
import io.limberapp.framework.exceptionMapping.ExceptionMappingConfigurator
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import io.limberapp.framework.module.Module
import org.slf4j.event.Level
import java.util.UUID

@Suppress("TooManyFunctions")
abstract class LimberApp(
    protected val config: Config
) {

    fun bindToApplication(application: Application) = with(application) {
        configure()
        bindModules()
    }

    private fun Application.configure() {
        authentication()
        cors()
        dataConversion()
        defaultHeaders()
        compression()
        callLogging()
        contentNegotiation()
        statusPages()
    }

    protected open fun Application.authentication() {
        install(Authentication) {
            jwt {
                with(config.jwt) {
                    when {
                        requireSignature && domain != null -> verifier(UrlJwkProvider(config.jwt.domain))
                        !requireSignature && domain == null -> verifier(JWT.require(Algorithm.none()).build())
                        else -> error("Invalid JWT config")
                    }
                }
                validate { credential -> JWTPrincipal(credential.payload) }
            }
        }
    }

    protected open fun Application.cors() {
        install(CORS) {
            this.anyHost()
        }
    }

    protected open fun Application.dataConversion() {
        install(DataConversion) {
            this.convert(
                UUID::class,
                UuidConversionService()
            )
        }
    }

    protected open fun Application.defaultHeaders() {
        install(DefaultHeaders)
    }

    protected open fun Application.compression() {
        install(Compression)
    }

    protected open fun Application.callLogging() {
        install(CallLogging) {
            this.level = Level.INFO
        }
    }

    protected open fun Application.contentNegotiation() {
        install(ContentNegotiation) {
            this.register(
                ContentType.Application.Json,
                JacksonConverter(
                    LimberObjectMapper(
                        prettyPrint = true
                    )
                )
            )
        }
    }

    protected open fun Application.statusPages() {
        install(StatusPages) {
            ExceptionMappingConfigurator().configureExceptionMapping(this)
        }
    }

    @Suppress("SpreadOperator") // Okay to use here because it's at application startup.
    private fun Application.bindModules() {
        Guice.createInjector(listOf(getMainModule(this), *modules.toTypedArray()))
    }

    protected abstract fun getMainModule(application: Application): MainModule

    protected abstract val modules: List<Module>
}
