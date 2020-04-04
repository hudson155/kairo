package com.piperframework

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Stage
import com.piperframework.config.Config
import com.piperframework.contentNegotiation.JsonContentConverter
import com.piperframework.dataConversion.conversionService.UuidConversionService
import com.piperframework.exception.EndpointNotFound
import com.piperframework.exception.PiperException
import com.piperframework.exceptionMapping.ExceptionMapper
import com.piperframework.module.Module
import com.piperframework.module.ModuleWithLifecycle
import com.piperframework.serialization.Json
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
import io.ktor.features.HttpsRedirect
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
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
abstract class SimplePiperApp<C : Config>(application: Application, protected val config: C) :
    PiperApp<SimplePiperApp.Context>(application) {

    data class Context(val modules: List<ModuleWithLifecycle>)

    override fun onStart(application: Application): Context {

        // First, create the injector.
        val modules = getMainModules(application).plus(modules)
        val injector = Guice.createInjector(Stage.PRODUCTION, modules)

        // Then, configure the application.
        // Pass the injector because configuration may require services that are bound in the injector.
        application.configure(injector)

        // Configure routing. Static files, dynamic endpoints, then 404.
        if (config.serving.staticFiles.serve) {
            application.serveStaticFiles(config.serving.staticFiles.rootPath!!, "index.html")
        }
        registerEndpoints(injector)
        application.handle404()

        // Return the context
        return Context(modules)
    }

    override fun onStop(application: Application, context: Context) {
        context.modules.forEach { it.unconfigure() }
    }

    private fun Application.configure(injector: Injector) {
        httpsRedirect()
        authentication(injector)
        cors()
        dataConversion()
        defaultHeaders()
        compression()
        callLogging()
        contentNegotiation()
        statusPages()
    }

    protected fun Application.httpsRedirect() {
        if (config.serving.redirectHttpToHttps) {
            install(HttpsRedirect)
        }
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
            convert(UUID::class, conversionService(UuidConversionService))
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
                converter = JsonContentConverter(Json())
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

    protected abstract fun getMainModules(application: Application): List<ModuleWithLifecycle>

    protected abstract val modules: List<Module>

    private fun Application.handle404(): Routing {
        return routing {
            route("${config.serving.apiPathPrefix}/{...}") {
                handle { throw EndpointNotFound() }
            }
        }
    }
}
