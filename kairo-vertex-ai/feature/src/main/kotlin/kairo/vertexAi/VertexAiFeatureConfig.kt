package kairo.vertexAi

import kotlinx.serialization.Serializable

@Serializable
public data class VertexAiFeatureConfig(
  val project: String? = null,
  val location: String? = project?.let { "global" },
)
