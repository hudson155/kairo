package kairo.slack

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import kairo.protectedString.ProtectedString

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(KairoSlackConfig.Noop::class, name = "Noop"),
  JsonSubTypes.Type(KairoSlackConfig.Real::class, name = "Real"),
)
public sealed class KairoSlackConfig {
  /**
   * See [NoopSlackClient].
   */
  public data object Noop : KairoSlackConfig()

  /**
   * See [RealSlackClient].
   */
  public data class Real(
    val token: ProtectedString,
    val channels: Map<String, String>,
  ) : KairoSlackConfig()
}
