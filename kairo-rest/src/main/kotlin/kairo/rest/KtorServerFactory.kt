package kairo.rest

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kairo.feature.Feature

private val logger: KLogger = KotlinLogging.logger {}

// TODO: This is not too configurable, and it should be.
internal object KtorServerFactory {
  fun create(
    config: RestFeatureConfig,
    features: List<Feature>,
  ): KtorServer =
    embeddedServer(
      factory = Netty,
      environment = applicationEnvironment(),
      configure = {
        // TODO: Configure this.
        connector {
          host = config.connector.host
          port = config.connector.port
        }
        shutdownGracePeriod = config.lifecycle.shutdownGracePeriod.inWholeMilliseconds
        shutdownTimeout = config.lifecycle.shutdownTimeout.inWholeMilliseconds
      },
      module = {
        // TODO: Configure this.
        install(CallLogging)
        install(ContentNegotiation) {
          json()
        }
        routing {
          features.forEach { feature ->
            if (feature !is RestFeature.HasRouting) return@forEach
            logger.info { "Registering routes (featureName=${feature.name})." }
            with(feature) { routing() }
          }
        }
      },
    )
}
