package kairo.stytch

import kairo.protectedString.ProtectedString
import kotlinx.serialization.Serializable

@Serializable
public data class StytchFeatureConfig(
  val projectId: String,
  val secret: ProtectedString,
)
