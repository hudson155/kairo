package kairo.rest

import com.google.inject.Binder
import com.google.inject.Injector
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kairo.dependencyInjection.bind
import kairo.dependencyInjection.getInstance
import kairo.dependencyInjection.toProvider
import kairo.rest.server.KtorServer
import kairo.rest.server.KtorServerProvider

private val logger: KLogger = KotlinLogging.logger {}

public open class KairoRestFeature(
  private val config: KairoRestConfig,
  private val module: KtorModuleFunction = KtorModuleFunction {},
) : BaseKairoRestFeature() {
  override fun bind(binder: Binder) {
    super.bind(binder)
    binder.bind<KairoRestConfig>().toInstance(config)
    binder.bind<KtorModuleFunction>().toInstance(module)
    binder.bind<KtorServer>().toProvider(KtorServerProvider::class)
  }

  override fun start(injector: Injector) {
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
