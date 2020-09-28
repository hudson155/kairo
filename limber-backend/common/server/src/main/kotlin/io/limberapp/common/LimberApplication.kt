package io.limberapp.common

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Stage
import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.application.ApplicationStopPreparing
import io.ktor.application.ApplicationStopping
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
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.JacksonConverter
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.common.exception.EndpointNotFound
import io.limberapp.common.ktorAuth.limberAuth
import io.limberapp.common.module.ApplicationModule
import io.limberapp.common.module.GuiceModule
import io.limberapp.common.serialization.Json
import io.limberapp.common.util.conversionService
import io.limberapp.config.Config
import io.limberapp.exception.LimberException
import io.limberapp.exceptionMapping.ExceptionMapper
import io.limberapp.module.main.MainModule
import io.limberapp.monolith.authentication.jwt.JwtAuthVerifier
import io.limberapp.typeConversion.conversionService.TimeZoneConversionService
import io.limberapp.typeConversion.conversionService.UuidConversionService
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.time.ZoneId
import java.util.*
import java.util.concurrent.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

@Suppress("TooManyFunctions")
abstract class LimberApplication<C : Config>(application: Application, protected val config: C) {
  private val logger = LoggerFactory.getLogger(LimberApplication::class.java)

  var context: Pair<Injector, List<GuiceModule>>? = null
    private set

  init {
    application.environment.monitor.subscribe(ApplicationStarted) { onStartInternal(it) }
    application.environment.monitor.subscribe(ApplicationStopping) { onStopInternal() }
  }

  private fun onStartInternal(application: Application) {
    logger.info("LimberApp starting...")
    @Suppress("TooGenericExceptionCaught")
    try {
      check(context == null)
      val newContext = onStart(application)
      context = newContext
      afterStart(application, newContext.first)
    } catch (e: Throwable) {
      logger.error("An error occurred during application startup. Shutting down...", e)
      application.shutDown(1)
    }
  }

  private fun onStopInternal() {
    logger.info("LimberApp stopping...")
    context?.let { onStop(it.second) }
    context = null
  }

  private fun onStart(application: Application): Pair<Injector, List<GuiceModule>> {
    // First, create the injector.
    val modules = getApplicationModules() + getAdditionalModules() + MainModule(application, config)
    val injector = Guice.createInjector(Stage.PRODUCTION, modules)

    // Then, configure the application.
    // Pass the injector because configuration may require services that are bound in the injector.
    application.configure()

    // Configure routing. Dynamic endpoints, then 404.
    registerEndpoints(injector)
    application.handle404()

    // Return the context.
    return Pair(injector, modules)
  }

  protected abstract fun getApplicationModules(): List<ApplicationModule>

  protected open fun getAdditionalModules(): List<GuiceModule> = emptyList()

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

  private fun Application.authentication() {
    install(Authentication) {
      configureAuthentication()
    }
  }

  private fun Authentication.Configuration.configureAuthentication() {
    limberAuth<Jwt> {
      verifier(
        scheme = JwtAuthVerifier.scheme,
        verifier = JwtAuthVerifier(config.authentication),
        default = true,
      )
    }
  }

  private fun Application.cors() {
    install(CORS) {
      HttpMethod.DefaultMethods.forEach { method(it) }
      allowSameOrigin = false
      anyHost()
      header(HttpHeaders.Authorization)
      header(HttpHeaders.ContentType)
    }
  }

  private fun Application.dataConversion() {
    install(DataConversion) {
      convert(ZoneId::class, conversionService(TimeZoneConversionService))
      convert(UUID::class, conversionService(UuidConversionService))
    }
  }

  private fun Application.defaultHeaders() {
    install(DefaultHeaders)
  }

  private fun Application.compression() {
    install(Compression)
  }

  private fun Application.callLogging() {
    install(CallLogging) {
      level = Level.INFO
    }
  }

  private fun Application.contentNegotiation() {
    install(ContentNegotiation) {
      val json = Json(prettyPrint = true)
      register(
        contentType = ContentType.Application.Json,
        converter = JacksonConverter(json.objectMapper)
      )
    }
  }

  private fun Application.statusPages() {
    install(StatusPages) {
      this.exception(LimberException::class.java) {
        val error = ExceptionMapper.handle(it)
        call.respond(HttpStatusCode.fromValue(error.statusCode), error)
      }
    }
  }

  private fun registerEndpoints(injector: Injector) {
    getApplicationModules().forEach { module ->
      module.endpoints.forEach {
        val endpoint = injector.getInstance(it)
        endpoint.register()
      }
    }
  }

  private fun Application.handle404(): Routing {
    return routing {
      route("/{...}") {
        handle { throw EndpointNotFound() }
      }
    }
  }

  protected open fun afterStart(application: Application, injector: Injector) {}

  private fun onStop(modules: List<GuiceModule>) {
    modules.forEach { it.unconfigure() }
  }
}

/**
 * Implementation adapted from [io.ktor.server.engine.ShutDownUrl].
 */
fun Application.shutDown(status: Int) {
  val latch = CountDownLatch(1)
  thread {
    @Suppress("MagicNumber")
    latch.await(10L, TimeUnit.SECONDS)
    environment.monitor.raise(ApplicationStopPreparing, environment)
    (environment as? ApplicationEngineEnvironment)?.stop() ?: dispose()
    exitProcess(status)
  }
  latch.countDown()
}
