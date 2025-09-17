package kairo.vertexAi

import com.google.genai.Client
import kairo.feature.Feature

public class VertexAiFeature(
  private val config: VertexAiFeatureConfig,
  private val block: Client.Builder.() -> Unit = {},
) : Feature() {
  override val name: String = "Vertex AI"

  private var client: Client? = null

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
