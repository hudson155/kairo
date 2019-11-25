package com.piperframework

import com.auth0.jwt.exceptions.JWTVerificationException
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.piperframework.authentication.PiperJwtVerifierProvider
import com.piperframework.config.Config
import com.piperframework.dataConversion.conversionService.UuidConversionService
import com.piperframework.exceptionMapping.ExceptionMappingConfigurator
import com.piperframework.jackson.objectMapper.PiperObjectMapper
import com.piperframework.ktorAuth.PiperAuthPrincipal
import com.piperframework.ktorAuth.PiperAuthVerifier
import com.piperframework.ktorAuth.piperAuth
import com.piperframework.module.Module
import com.piperframework.util.conversionService
import com.piperframework.util.serveStaticFiles
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
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
import org.slf4j.event.Level
import java.util.UUID

/**
 * This class has a lot of functions, but it's clearer this way.
 */
@Suppress("TooManyFunctions")
abstract class PiperApp<C : Config>(protected val config: C) {

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
            val provider = PiperJwtVerifierProvider(config.authentication)
            piperAuth {
                verifier("Bearer", object : PiperAuthVerifier {
                    override fun verify(blob: String): PiperAuthPrincipal? {
                        val payload = try {
                            provider.getVerifier(blob)?.verify(blob)
                        } catch (_: JWTVerificationException) {
                            null
                        } ?: return null
                        return PiperAuthPrincipal(payload)
                    }
                }, default = true)
            }
        }
    }

    protected open fun Application.cors() {
        install(CORS) {
            allowSameOrigin = false
            anyHost()
            header(HttpHeaders.Authorization)
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
                converter = JacksonConverter(PiperObjectMapper(prettyPrint = true))
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
