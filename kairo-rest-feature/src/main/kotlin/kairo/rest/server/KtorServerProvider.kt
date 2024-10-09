package kairo.rest.server

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.cio.CIO
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import kairo.dependencyInjection.LazySingletonProvider
import kairo.rest.KairoRestConfig
import kairo.rest.KtorModuleFunction
import kairo.rest.auth.AuthVerifier
import kairo.rest.handler.RestHandler

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
  private val authVerifiers: List<AuthVerifier<*>>,
  private val handlers: Set<RestHandler<*, *>>,
  private val module: KtorModuleFunction,
) : LazySingletonProvider<KtorServer>() {
  override fun create(): KtorServer =
    embeddedServer(
      factory = CIO,
      environment = applicationEnvironment(),
      configure = configureEmbeddedServer(config),
      module = {
        installAuth(authVerifiers)
        installContentNegotiation()
        installCors(config.cors)
        installStatusPages()
        registerRestHandlers(handlers)
        with(module) {
          module()
        }
      },
    )
}
