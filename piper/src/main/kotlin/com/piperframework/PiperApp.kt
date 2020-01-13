package com.piperframework

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.piperframework.config.Config
import com.piperframework.dataConversion.conversionService.UuidConversionService
import com.piperframework.exception.EndpointNotFound
import com.piperframework.exception.PiperException
import com.piperframework.exceptionMapping.ExceptionMapper
import com.piperframework.jackson.objectMapper.PiperObjectMapper
import com.piperframework.module.Module
import com.piperframework.util.conversionService
import com.piperframework.util.serveStaticFiles
import io.ktor.application.Application
import io.ktor.application.call
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
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.JacksonConverter
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.routing.routing
import org.slf4j.event.Level
import java.util.UUID

/**
 * This class has a lot of functions, but it's clearer this way.
 */
@Suppress("TooManyFunctions")
abstract class PiperApp<C : Config>(protected val config: C) {

    fun bindToApplication(application: Application) {
        with(application) {
            val injector = bindModules()
            configure(injector)
            if (config.serving.staticFiles.serve) {
                serveStaticFiles(config.serving.staticFiles.rootPath!!, "index.html")
            }
            registerEndpoints(injector)
            handle404()
        }
    }

    private fun Application.bindModules(): Injector = Guice.createInjector(getMainModules(this).plus(modules))

    private fun Application.configure(injector: Injector) {
        authentication(injector)
        cors()
        dataConversion()
        defaultHeaders()
        compression()
        callLogging()
        contentNegotiation()
        statusPages()
    }

    protected fun Application.authentication(injector: Injector) {
        install(Authentication) {
            configureAuthentication(injector)
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
            val exceptionMapper = ExceptionMapper()
            this.exception(PiperException::class.java) {
                val error = exceptionMapper.handle(it)
                call.respond(HttpStatusCode.fromValue(error.statusCode), error)
            }
        }
    }

    private fun registerEndpoints(injector: Injector) {
        modules.forEach { module ->
            module.endpoints.forEach {
                val endpoint = injector.getInstance(it)
                endpoint.register()
            }
        }
    }

    protected abstract fun Authentication.Configuration.configureAuthentication(injector: Injector)

    protected abstract fun getMainModules(application: Application): List<AbstractModule>

    protected abstract val modules: List<Module>

    private fun Application.handle404(): Routing {
        return routing {
            route("${config.serving.apiPathPrefix}/{...}") {
                handle { throw EndpointNotFound() }
            }
        }
    }
}
