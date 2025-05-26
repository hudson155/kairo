package kairo.rest.server

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kairo.dependencyInjection.LazySingletonProvider
import kairo.rest.KairoRestConfig
import kairo.rest.KtorModuleFunction
import kairo.rest.exceptionHandler.ExceptionManager
import kairo.rest.handler.RestHandler

/**
 * There is a single global [KtorServer] instance in Kairo.
 */
@Singleton
internal class KtorServerProvider @Inject constructor(
  private val config: KairoRestConfig,
  private val exceptionManager: ExceptionManager,
  private val handlers: Set<RestHandler<*, *>>,
  private val module: KtorModuleFunction,
) : LazySingletonProvider<KtorServer>() {
  override fun create(): KtorServer =
    embeddedServer(
      factory = Netty,
      environment = applicationEnvironment(),
      configure = configureEmbeddedServer(config),
      module = {
        useCallStartTime()
        installContentNegotiation()
        installStatusPages(exceptionManager)
        with(module) {
          module()
        }
        registerRestHandlers(handlers)
      },
    )
}
