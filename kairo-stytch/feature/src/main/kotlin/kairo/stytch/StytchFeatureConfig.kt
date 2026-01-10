package kairo.stytch

import kairo.protectedString.ProtectedString

public data class StytchFeatureConfig(
  val projectId: String,
  val secret: ProtectedString,
)
