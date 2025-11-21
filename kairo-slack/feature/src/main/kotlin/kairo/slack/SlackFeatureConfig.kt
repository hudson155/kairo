package kairo.slack

import kairo.protectedString.ProtectedString
import kotlinx.serialization.Serializable

@Serializable
public data class SlackFeatureConfig(
  val token: ProtectedString,
  val channels: Map<String, String>,
)
