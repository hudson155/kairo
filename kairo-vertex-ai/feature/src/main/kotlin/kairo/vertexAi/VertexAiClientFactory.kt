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
      config.project?.let { project(it) }
      config.location?.let { location(it) }
      vertexAI(true)
      block()
    }.build()
}
