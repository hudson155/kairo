package limber.feature.rest

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import io.ktor.server.application.Application
import io.ktor.server.auth.principal
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import jakarta.validation.Validator
import limber.auth.RestContext
import limber.auth.RestContextFactory
import limber.config.rest.RestConfig
import limber.feature.Feature
import limber.feature.FeaturePriority
import limber.feature.rest.ktorPlugins.installPlugins
import limber.validation.ValidatorProvider
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
        gracePeriodMillis = config.shutDown.gracePeriodMs,
        timeoutMillis = config.shutDown.timeoutMs,
      )
    }
  }

  private fun module(injector: Injector): Application.() -> Unit = {
    applicationContext = ApplicationContext(config, injector)
    installPlugins()
    registerEndpoints()
  }
}
