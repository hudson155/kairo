package kairo.rest

import com.google.inject.Inject
import io.ktor.server.cio.CIO
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import kairo.dependencyInjection.LazySingletonProvider

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
