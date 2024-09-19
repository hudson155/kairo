package kairo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import kairo.protectedString.ProtectedString
import kairo.serialization.module.primitives.StringDeserializer
import kairo.serialization.module.primitives.TrimWhitespace

/**
 * Adds support for [String] from various [ConfigLoaderSource]s.
 */
internal class ConfigStringDeserializer(
  config: ConfigLoaderConfig,
) : ConfigDeserializer<String>(String::class, config) {
  private val stringDeserializer: StringDeserializer = StringDeserializer(TrimWhitespace.Type.TrimNone)

  /**
   * In addition to the [ConfigLoaderSource]s, strings can be plaintext.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String? {
    if (p.currentToken in stringDeserializer.tokens) {
      return stringDeserializer.deserialize(p, ctxt)
    }
    return super.deserialize(p, ctxt)
  }

  override fun isSecure(source: ConfigLoaderSource): Boolean =
    when (source) {
      is ConfigLoaderSource.Command -> false // The command source is always unsafe.
      is ConfigLoaderSource.EnvironmentVariable -> true
      is ConfigLoaderSource.GcpSecret -> false // GCP secrets are assumed to contain sensitive data.
    }

  @OptIn(ProtectedString.Access::class)
  override fun convert(string: ProtectedString): String =
    string.value
}
