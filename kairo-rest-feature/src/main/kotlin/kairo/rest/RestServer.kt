package kairo.rest

import com.google.inject.Inject
import com.google.inject.ProvidedBy
import com.google.inject.Singleton
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer

private val logger: KLogger = KotlinLogging.logger {}

private typealias Server = EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>

/**
 * A simple wrapper around Ktor's [embeddedServer] function,
 * adding support for easy [start] and [stop].
 */
@Singleton
@ProvidedBy(RestServer.Provider::class)
internal class RestServer(private val ktor: Server) {
  internal class Provider @Inject constructor(
    private val config: KairoRestConfig,
    private val handlers: Set<RestHandler<*, *>>,
  ) : com.google.inject.Provider<RestServer> {
    override fun get(): RestServer {
      val ktor = createKtor()
      return RestServer(ktor)
    }

    private fun createKtor(): Server =
      embeddedServer(
        factory = CIO,
        environment = applicationEnvironment(),
        configure = configureEmbeddedServer(config),
        module = createModule(handlers),
      )
  }

  fun start() {
    logger.info { "Starting REST server." }
    ktor.start()
  }

  fun stop() {
    logger.info { "Stopping REST server." }
    ktor.stop()
  }
}
