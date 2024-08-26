package kairo.restFeature

import com.google.inject.Inject
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer

private val logger: KLogger = KotlinLogging.logger {}

/**
 * A simple wrapper around Ktor's [embeddedServer] function,
 * adding support for easy [start] and [stop].
 */
internal class RestServer @Inject constructor(
  private val config: KairoRestConfig,
  private val handlers: Set<RestHandler<*, *>>,
) {
  private var ktor: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>? = null

  fun start() {
    logger.info { "Starting Ktor." }
    val ktor = embeddedServer(
      factory = CIO,
      environment = applicationEnvironment(),
      configure = configureEmbeddedServer(config),
      module = createModule(handlers),
    )
    this.ktor = ktor
    ktor.start()
  }

  fun stop() {
    logger.info { "Stopping Ktor." }
    ktor?.stop()
    ktor = null
  }
}
