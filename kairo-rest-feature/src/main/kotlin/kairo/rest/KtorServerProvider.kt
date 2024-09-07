package kairo.rest

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.cio.CIO
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import kairo.dependencyInjection.LazySingletonProvider

/**
 * There is a single global [KtorServer] instance in Kairo.
 *
 * [CIO] is Ktor's custom coroutine-based I/O application engine factory.
 * This is an alternative to Netty; [CIO] uses coroutines instead of threads for handling HTTP requests.
 * There are some potential footguns with [CIO] when interacting with other libraries.
 * See the Feature README for more information.
 */
@Singleton
internal class KtorServerProvider @Inject constructor(
  private val config: KairoRestConfig,
  private val handlers: Set<RestHandler<*, *>>,
) : LazySingletonProvider<KtorServer>() {
  override fun create(): KtorServer =
    embeddedServer(
      factory = CIO,
      environment = applicationEnvironment(),
      configure = configureEmbeddedServer(config),
      module = createModule(handlers),
    )
}
