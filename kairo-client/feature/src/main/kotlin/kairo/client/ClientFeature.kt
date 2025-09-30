package kairo.client

import io.ktor.client.HttpClientConfig
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kotlin.time.Duration
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Create a separate Feature for each external integration.
 */
public abstract class ClientFeature(
  httpClientName: String,
) : Feature(), HasKoinModules {
  protected abstract val timeout: Duration

  override val koinModules: List<Module> =
    listOf(
      module {
        single(named(httpClientName)) {
          HttpClientFactory.create(
            timeout = timeout,
            block = { configure() },
          )
        }
      },
    )

  protected open fun HttpClientConfig<*>.configure(): Unit =
    Unit
}
