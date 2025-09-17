package kairo.vertexAi

import com.google.genai.Client
import kairo.dependencyInjection.KoinModule
import kairo.feature.Feature
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * The Vertex AI Feature provides access to Google Generative AI through Vertex AI.
 */
public class VertexAiFeature(
  private val config: VertexAiFeatureConfig,
  private val block: Client.Builder.() -> Unit = {},
) : Feature(), KoinModule {
  override val name: String = "Vertex AI"

  private var client: Client? = null

  override val koinModule: Module =
    module {
      factory<Client> { checkNotNull(client) }
    }

  override suspend fun start(features: List<Feature>) {
    val client = VertexAiClientFactory.create(config, block)
    this.client = client
  }

  override suspend fun stop(features: List<Feature>) {
    this.client?.let { client ->
      client.close()
      this.client = null
    }
  }

  public companion object
}

/**
 * TODO: Introduce integration testing support for this.
 *  There could be 2 options:
 *  1. Mocks and/or no-op.
 *  2. Really hit genai backend.
 */
