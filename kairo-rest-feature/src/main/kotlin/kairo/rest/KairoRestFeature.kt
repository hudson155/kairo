package kairo.rest

import com.google.inject.Binder
import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getInstance
import kairo.dependencyInjection.toProvider
import kairo.feature.Feature

private val logger: KLogger = KotlinLogging.logger {}

public class KairoRestFeature(
  private val config: KairoRestConfig,
) : BaseKairoRestFeature() {
  override fun bind(binder: Binder) {
    binder.bind<KairoRestConfig>().toInstance(config)
    binder.bind<KtorServer>().toProvider(KtorServerProvider::class)
    super.bind(binder)
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
