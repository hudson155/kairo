package kairo.mailersend

import kairo.protectedString.ProtectedString

public data class MailersendFeatureConfig(
  val apiToken: ProtectedString,
  val templates: Map<String, String>,
)
