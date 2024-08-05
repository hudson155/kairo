package kairo.restFeature

import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kotlin.concurrent.withLock

public class RestFeature(
  private val config: RestConfig,
) : Feature() {
  private val logger: KLogger = KotlinLogging.logger {}

  override val name: String = "REST"

  override val priority: FeaturePriority = FeaturePriority.Framework

  private var ktor: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>? = null

  override fun start(injector: Injector, features: Set<Feature>) {
    if (ktor != null) {
      logger.warn { "Ktor already started." }
      return
    }
    logger.info { "Starting Ktor." }

    val ktor = embeddedServer(
      factory = CIO,
      environment = applicationEnvironment(),
      configure = configureEmbeddedServer(config),
      module = {}
    )
    this.ktor = ktor
    ktor.start()
  }

  override fun stop(injector: Injector?) {
    ktor?.stop()
    ktor = null
  }
}
