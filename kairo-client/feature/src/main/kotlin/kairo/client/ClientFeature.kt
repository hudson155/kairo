package kairo.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kairo.feature.LifecycleHandler
import kairo.feature.lifecycle
import kairo.optional.OptionalModule
import kairo.serialization.KairoJson
import kotlin.time.Duration
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Extend this to create Ktor-native HTTP clients.
 * Create a separate Feature for each external integration.
 */
public abstract class ClientFeature(
  httpClientName: String,
) : Feature(), HasKoinModules {
  protected open val json: KairoJson =
    KairoJson {
      addModule(OptionalModule())
    }

  protected abstract val timeout: Duration

  private val httpClient: HttpClient by lazy {
    @Suppress("MissingUseCall")
    HttpClientFactory.create(
      timeout = timeout,
      json = json,
      block = { configure() },
    )
  }

  override val koinModules: List<Module> =
    listOf(
      module {
        single(named(httpClientName)) { httpClient }
      },
    )

  override val lifecycle: List<LifecycleHandler> =
    lifecycle {
      handler {
        start { httpClient }
        stop { httpClient.close() }
      }
    }

  protected open fun HttpClientConfig<*>.configure(): Unit =
    Unit
}
