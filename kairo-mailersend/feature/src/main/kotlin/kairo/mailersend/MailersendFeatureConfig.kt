package kairo.mailersend

import kairo.protectedString.ProtectedString
import kotlinx.serialization.Serializable

@Serializable
public data class MailersendFeatureConfig(
  val apiToken: ProtectedString,
  val templates: Map<String, String>,
)
