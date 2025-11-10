package kairo.client

import io.ktor.client.HttpClientConfig
import kairo.dependencyInjection.HasKoinModules
import kairo.feature.Feature
import kotlin.time.Duration
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
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
  protected abstract val timeout: Duration

  override val koinModules: List<Module> =
    listOf(
      module {
        single(named(httpClientName)) {
          @Suppress("MissingUseCall")
          HttpClientFactory.create(
            timeout = timeout,
            configureJson = { configure() },
            block = { configure() },
          )
        }
      },
    )

  /**
   * Configure the [Json] instance.
   */
  protected open fun JsonBuilder.configure(): Unit =
    Unit

  /**
   * Configure the HTTP client.
   */
  protected open fun HttpClientConfig<*>.configure(): Unit =
    Unit
}
