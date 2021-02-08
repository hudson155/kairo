package io.limberapp.common.server

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Stage
import io.ktor.application.Application
import io.ktor.auth.authenticate
import io.ktor.routing.route
import io.ktor.routing.routing
import io.limberapp.common.config.Config
import io.limberapp.common.exception.EndpointNotFound
import io.limberapp.common.module.Feature
import io.limberapp.common.module.Module
import io.limberapp.common.module.typeConverters
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.route
import io.limberapp.common.serialization.LimberObjectMapper
import io.limberapp.common.server.feature.configureAuthentication
import io.limberapp.common.server.feature.configureCallLogging
import io.limberapp.common.server.feature.configureCompression
import io.limberapp.common.server.feature.configureContentNegotiation
import io.limberapp.common.server.feature.configureCors
import io.limberapp.common.server.feature.configureDataConversion
import io.limberapp.common.server.feature.configureDefaultHeaders
import io.limberapp.common.server.feature.configureStatusPages
import io.limberapp.common.typeConversion.TypeConverter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

@Suppress("LateinitUsage")
abstract class Server<C : Config>(
    application: Application,
    protected val config: C,
) {
  private val logger: Logger = LoggerFactory.getLogger(Server::class.java)

  abstract val modules: Set<Module>

  lateinit var injector: Injector

  init {
    KtorDelegateServer(application, configure = ::configure, cleanUp = ::cleanUp)
  }

  private fun configure(application: Application) {
    val typeConverters: Set<TypeConverter<*>> = modules.typeConverters
    val guiceModules: Set<AbstractModule> = run {
      val objectMapper = LimberObjectMapper(typeConverters = typeConverters)
      val mainModule = MainModule(config, objectMapper)
      return@run modules + mainModule
    }
    val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> =
        modules.flatMap { (it as? Feature)?.apiEndpoints.orEmpty() }

    injector = Guice.createInjector(Stage.PRODUCTION, guiceModules)
    val objectMapper = injector.getInstance(LimberObjectMapper::class.java)

    application.configureAuthentication(config.authentication, objectMapper)
    application.configureCors()
    application.configureDataConversion(typeConverters)
    application.configureDefaultHeaders()
    application.configureCompression()
    application.configureCallLogging()
    application.configureContentNegotiation(objectMapper)
    application.configureStatusPages()

    application.registerEndpoints(apiEndpoints.map { injector.getInstance(it.java) })
    application.handle404()
  }

  private fun Application.registerEndpoints(apiEndpoints: List<EndpointHandler<*, *>>) {
    apiEndpoints.forEach { apiEndpoint ->
      with(apiEndpoint) {
        logger.info("Registering $template.")
        routing {
          authenticate(optional = true) {
            route(template) {
              handle()
            }
          }
        }
      }
    }
  }

  private fun Application.handle404() {
    routing {
      route("/{...}") {
        handle { throw EndpointNotFound() }
      }
    }
  }

  private fun cleanUp() {
    modules.forEach { it.cleanUp() }
  }
}
