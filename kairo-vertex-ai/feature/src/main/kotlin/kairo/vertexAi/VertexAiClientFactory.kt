package kairo.vertexAi

import com.google.auth.oauth2.GoogleCredentials
import com.google.genai.Client

internal object VertexAiClientFactory {
  @Suppress("ForbiddenMethodCall")
  fun fromEnvironment(block: Client.Builder.() -> Unit = {}): Client =
    create(
      config = VertexAiFeatureConfig(
        project = System.getenv("GCLOUD_PROJECT"),
        location = System.getenv("GCLOUD_LOCATION"),
      ),
      block = {
        System.getenv("GCP_CREDENTIALS")?.let { credentials ->
          credentials(GoogleCredentials.fromStream(credentials.byteInputStream()))
        }
        block()
      },
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
