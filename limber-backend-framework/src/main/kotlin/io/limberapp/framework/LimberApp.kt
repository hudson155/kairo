package io.limberapp.framework

import com.google.inject.AbstractModule
import com.google.inject.Guice
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.limberapp.framework.ktorAuth.JWTPrincipal
import io.limberapp.framework.ktorAuth.jwt
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.jackson.JacksonConverter
import io.limberapp.framework.authentication.LimberJwtVerifierProvider
import io.limberapp.framework.config.Config
import io.limberapp.framework.dataConversion.conversionService.UuidConversionService
import io.limberapp.framework.exceptionMapping.ExceptionMappingConfigurator
import io.limberapp.framework.jackson.objectMapper.LimberObjectMapper
import io.limberapp.framework.module.Module
import io.limberapp.framework.util.conversionService
import io.limberapp.framework.util.serveStaticFiles
import org.slf4j.event.Level
import java.util.UUID

/**
 * This class has a lot of functions, but it's clearer this way.
 */
@Suppress("TooManyFunctions")
abstract class LimberApp<C : Config>(
    protected val config: C
) {

    fun bindToApplication(application: Application) = with(application) {
        configure()
        bindModules()
        if (config.serving.staticFiles.serve) {
            serveStaticFiles(config.serving.staticFiles.rootPath!!, "index.html")
        }
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
                verifier(LimberJwtVerifierProvider(config.authentication)::getVerifier)
                validate { credential -> JWTPrincipal(credential.payload) }
            }
        }
    }

    protected open fun Application.cors() {
        install(CORS) {
            this.allowSameOrigin = false
            this.anyHost()
            this.header(HttpHeaders.Authorization)
        }
    }

    protected open fun Application.dataConversion() {
        install(DataConversion) {
            convert(UUID::class, conversionService(UuidConversionService()))
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
            level = Level.INFO
        }
    }

    protected open fun Application.contentNegotiation() {
        install(ContentNegotiation) {
            register(
                contentType = ContentType.Application.Json,
                converter = JacksonConverter(LimberObjectMapper(prettyPrint = true))
            )
        }
    }

    protected open fun Application.statusPages() {
        install(StatusPages) {
            ExceptionMappingConfigurator().configureExceptionMapping(this)
        }
    }

    private fun Application.bindModules() {
        Guice.createInjector(getMainModules(this).plus(modules))
    }

    protected abstract fun getMainModules(application: Application): List<AbstractModule>

    protected abstract val modules: List<Module>
}
