package kairo.slack

import kairo.protectedString.ProtectedString

public data class SlackFeatureConfig(
  val token: ProtectedString,
  val channels: Map<String, String>,
)
