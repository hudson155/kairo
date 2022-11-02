package limber.feature.rest

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.HttpAcceptRouteSelector
import io.ktor.server.routing.HttpMethodRouteSelector
import io.ktor.server.routing.ParameterRouteSelector
import io.ktor.server.routing.Route
import io.ktor.server.routing.createRouteFromPath
import io.ktor.server.routing.routing
import jakarta.validation.Validator
import limber.auth.RestContext
import limber.auth.RestContextFactory
import limber.config.rest.RestConfig
import limber.feature.Feature
import limber.feature.FeaturePriority
import limber.feature.rest.ktorPlugins.installPlugins
import mu.KLogger
import mu.KotlinLogging

public open class RestFeature(private val config: RestConfig) : Feature() {
  private val logger: KLogger = KotlinLogging.logger {}

  override val priority: FeaturePriority = FeaturePriority.Framework

  private var ktor: ApplicationEngine? = null

  override fun bind(binder: PrivateBinder) {
    binder.bind(RestContextFactory::class.java).toProvider {
      RestContextFactory { call ->
        val authConfig = config.auth
        return@RestContextFactory RestContext(
          authorize = authConfig != null,
          claimPrefix = authConfig?.let { it.jwtClaimPrefix }.orEmpty(),
          principal = call.principal(),
        )
      }
    }
    binder.expose(RestContextFactory::class.java)

    binder.bind(Validator::class.java).toProvider(ValidatorProvider::class.java)
    binder.expose(Validator::class.java)
  }

  override fun start(injector: Injector, features: Set<Feature>) {
    if (ktor != null) {
      logger.error { "Was going to start Ktor, but it was already started." }
    }

    logger.info { "Creating Ktor application engine..." }
    ktor = embeddedServer(
      factory = CIO,
      port = config.port,
      watchPaths = emptyList(),
      configure = {
        connectionGroupSize = config.parallelization.connectionGroupSize
        workerGroupSize = config.parallelization.workerGroupSize
        callGroupSize = config.parallelization.callGroupSize
      },
      module = module(injector),
    ).start()
  }

  override fun stop() {
    ktor?.let { engine ->
      logger.info { "Stopping Ktor application engine..." }
      engine.stop(
        gracePeriodMillis = config.shutDown.gracePeriodMillis,
        timeoutMillis = config.shutDown.timeoutMillis,
      )
    }
  }

  private fun module(injector: Injector): Application.() -> Unit = {
    installPlugins(config)

    val handlers = injector.bindings.mapNotNull { (key, binding) ->
      val isEndpointHandler = RestEndpointHandler::class.java.isAssignableFrom(key.typeLiteral.rawType)
      if (!isEndpointHandler) return@mapNotNull null
      return@mapNotNull binding.provider.get() as RestEndpointHandler<*, *>
    }

    handlers.forEach { handler ->
      val template = handler.template
      logger.info { "Registering endpoint: $template." }
      routing {
        optionallyAuthenticate(config.auth != null) {
          createRouteFromPath(template.path)
            .createChild(HttpAcceptRouteSelector(ContentType.Application.Json))
            .createChild(HttpMethodRouteSelector(template.method))
            .let {
              template.requiredQueryParams.fold(it) { route, queryParam ->
                route.createChild(ParameterRouteSelector(queryParam))
              }
            }
            .handle { handler.handle(call) }
        }
      }
    }
  }
}

private fun Route.optionallyAuthenticate(authenticate: Boolean, build: Route.() -> Unit) {
  if (authenticate) authenticate(optional = true, build = build)
  else build()
}
