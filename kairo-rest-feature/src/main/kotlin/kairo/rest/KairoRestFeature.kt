package kairo.rest

import com.google.inject.Injector
import com.google.inject.PrivateBinder
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.expose
import kairo.dependencyInjection.getInstance
import kairo.dependencyInjection.toProvider
import kairo.feature.Feature
import kairo.feature.FeaturePriority

private val logger: KLogger = KotlinLogging.logger {}

public class KairoRestFeature(
  private val config: KairoRestConfig,
) : Feature() {
  override val name: String = "REST"

  override val priority: FeaturePriority = FeaturePriority.Framework

  override fun bind(binder: PrivateBinder) {
    binder.bind<KairoRestConfig>().toInstance(config)

    binder.bind<KtorServer>().toProvider(KtorServerProvider::class)
    binder.expose<KtorServer>()

    binder.bindRestHandlers { } // Supports Servers with no REST handlers.
  }

  override fun start(injector: Injector, features: Set<Feature>) {
    logger.info { "Starting Ktor REST server." }
    val server = injector.getInstance<KtorServer>()
    server.start()
  }

  override fun stop(injector: Injector?) {
    injector ?: return
    logger.info { "Stopping Ktor REST server." }
    injector.getInstance<KtorServer>().stop()
  }
}
