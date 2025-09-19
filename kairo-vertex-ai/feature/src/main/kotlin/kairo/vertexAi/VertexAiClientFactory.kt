package kairo.vertexAi

import com.google.genai.Client

internal object VertexAiClientFactory {
  @Suppress("ForbiddenMethodCall")
  fun fromEnvironment(block: Client.Builder.() -> Unit = {}): Client =
    create(
      config = VertexAiFeatureConfig(
        project = System.getenv("VERTEX_AI_PROJECT"),
        location = System.getenv("VERTEX_AI_LOCATION"),
      ),
      block = block,
    )

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
