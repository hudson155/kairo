package io.limberapp.common

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Stage
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
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.routing.routing
import io.limberapp.common.config.Config
import io.limberapp.common.contentNegotiation.JsonContentConverter
import io.limberapp.common.dataConversion.conversionService.TimeZoneConversionService
import io.limberapp.common.dataConversion.conversionService.UuidConversionService
import io.limberapp.common.exception.EndpointNotFound
import io.limberapp.common.exception.LimberException
import io.limberapp.common.exceptionMapping.ExceptionMapper
import io.limberapp.common.module.Module
import io.limberapp.common.module.ModuleWithLifecycle
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.forKtor
import io.limberapp.common.serialization.Json
import io.limberapp.common.types.TimeZone
import io.limberapp.common.util.conversionService
import org.slf4j.event.Level
import java.util.*

/**
 * A basic Limber app that implements most things by default. Unless an application needs heavy customization, it's
 * generally easiest to extend from this rather than [LimberApp].
 *
 * This class has a lot of functions, but it's clearer this way.
 */
@Suppress("TooManyFunctions")
abstract class SimpleLimberApp<C : Config>(
  application: Application,
  protected val config: C,
) : LimberApp<SimpleLimberApp.Context>(application) {
  data class Context(val injector: Injector, val modules: List<ModuleWithLifecycle>)

  override fun onStart(application: Application): Context {
    // First, create the injector.
    val modules = getMainModules(application) + modules
    val injector = Guice.createInjector(Stage.PRODUCTION, modules)

    // Then, configure the application.
    // Pass the injector because configuration may require services that are bound in the injector.
    application.configure(injector)

    // Configure routing. Dynamic endpoints, then 404.
    registerEndpoints(injector)
    application.handle404()

    // Return the context.
    return Context(injector, modules)
  }

  final override fun onStop(application: Application, context: Context) {
    context.modules.forEach { it.unconfigure() }
  }

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
      HttpMethod.values().forEach { method(it.forKtor()) }
      allowSameOrigin = false
      anyHost()
      header(HttpHeaders.Authorization)
      header(HttpHeaders.ContentType)
    }
  }

  protected open fun Application.dataConversion() {
    install(DataConversion) {
      convert(TimeZone::class, conversionService(TimeZoneConversionService))
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
      val json = Json(prettyPrint = true)
      register(
        contentType = ContentType.Application.Json,
        converter = JsonContentConverter(json)
      )
    }
  }

  protected open fun Application.statusPages() {
    install(StatusPages) {
      val exceptionMapper = ExceptionMapper()
      this.exception(LimberException::class.java) {
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
      route("/{...}") {
        handle { throw EndpointNotFound() }
      }
    }
  }
}
