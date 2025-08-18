package kairo.rest

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import kairo.feature.Feature

private val logger: KLogger = KotlinLogging.logger {}

// TODO: This is not too configurable, and it should be.
internal object KtorServerFactory {
  fun create(
    config: RestFeatureConfig,
    features: List<Feature>,
    ktorConfiguration: KtorServerConfig.() -> Unit,
  ): KtorServer =
    embeddedServer(
      factory = Netty,
      environment = applicationEnvironment(),
      configure = {
        runningLimit = config.parallelism.runningLimit
        shareWorkGroup = config.parallelism.shareWorkGroup
        config.parallelism.connectionGroupSize?.let { connectionGroupSize = it }
        config.parallelism.workerGroupSize?.let { workerGroupSize = it }
        config.parallelism.callGroupSize?.let { callGroupSize = it }
        requestReadTimeoutSeconds = config.timeouts.requestRead.inWholeSeconds.toInt()
        responseWriteTimeoutSeconds = config.timeouts.responseWrite.inWholeSeconds.toInt()
        maxInitialLineLength = 8 * 1024
        maxHeaderSize = 16 * 1024
        maxChunkSize = 8 * 1024
        shutdownGracePeriod = config.lifecycle.shutdownGracePeriod.inWholeMilliseconds
        shutdownTimeout = config.lifecycle.shutdownTimeout.inWholeMilliseconds
        connector {
          host = config.connector.host
          port = config.connector.port
        }
        ktorConfiguration()
      },
      module = {
        // TODO: Configure this.
        install(AutoHeadResponse)
        install(CallLogging)
        install(ContentNegotiation) {
          json()
        }
        install(Compression)
        install(Resources)
        features.forEach { feature ->
          if (feature !is RestFeature.HasRouting) return@forEach
          logger.info { "Registering routes (featureName=${feature.name})." }
          with(feature) { routing() }
        }
      },
    )
}
