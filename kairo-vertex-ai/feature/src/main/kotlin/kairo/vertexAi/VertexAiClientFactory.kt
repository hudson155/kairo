package kairo.vertexAi

import com.google.auth.oauth2.GoogleCredentials
import com.google.genai.Client

internal object VertexAiClientFactory {
  @Suppress("ForbiddenMethodCall")
  fun fromEnvironment(block: Client.Builder.() -> Unit = {}): Client {
    System.getenv().entries.joinToString("\n") { "${it.key}=${it.value}" }.let { error(System.getenv("GCLOUD_PROJECT") + it) }
    return create(
      config = VertexAiFeatureConfig(
        project = System.getenv("GCP_PROJECT"),
        location = System.getenv("GCP_LOCATION"),
      ),
      block = {
        System.getenv("GCP_CREDENTIALS")?.let { credentials ->
          credentials(GoogleCredentials.fromStream(credentials.byteInputStream()))
        }
        block()
      },
    )
  }

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
