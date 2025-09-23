package kairo.vertexAi

import com.google.genai.Client

internal object VertexAiClientFactory {
  fun create(
    config: VertexAiFeatureConfig,
    block: Client.Builder.() -> Unit,
  ): Client =
    Client.builder().apply {
      project(config.project)
      location(config.location)
      vertexAI(true)
      block()
    }.build()
}
