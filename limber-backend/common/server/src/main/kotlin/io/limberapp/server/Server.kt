package io.limberapp.server

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Stage
import io.ktor.application.Application
import io.ktor.auth.authenticate
import io.ktor.routing.route
import io.ktor.routing.routing
import io.limberapp.config.Config
import io.limberapp.module.Feature
import io.limberapp.module.Module
import io.limberapp.module.typeConverters
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.route
import io.limberapp.serialization.LimberObjectMapper
import io.limberapp.server.exception.EndpointNotFound
import io.limberapp.server.feature.configureAuthentication
import io.limberapp.server.feature.configureCallLogging
import io.limberapp.server.feature.configureCompression
import io.limberapp.server.feature.configureContentNegotiation
import io.limberapp.server.feature.configureCors
import io.limberapp.server.feature.configureDataConversion
import io.limberapp.server.feature.configureDefaultHeaders
import io.limberapp.server.feature.configureStatusPages
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
    val typeConverters = modules.typeConverters

    val guiceModules: Set<AbstractModule> = run {
      val mainModule = MainModule(config, typeConverters)
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
