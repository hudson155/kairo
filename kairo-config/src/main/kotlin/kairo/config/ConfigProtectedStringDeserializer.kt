package kairo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import kairo.protectedString.ProtectedString
import kairo.serialization.module.primitives.StringDeserializer
import kairo.serialization.module.primitives.TrimWhitespace

/**
 * Adds support for [ProtectedString] from various [ConfigLoaderSource]s.
 */
@OptIn(ProtectedString.Access::class)
internal class ConfigProtectedStringDeserializer(
  config: ConfigLoaderConfig,
) : ConfigDeserializer<ProtectedString>(ProtectedString::class, config) {
  private val stringDeserializer: StringDeserializer = StringDeserializer(TrimWhitespace.Type.TrimNone)

  /**
   * In addition to the [ConfigLoaderSource]s, protected strings can be plaintext.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ProtectedString? {
    if (p.currentToken in stringDeserializer.tokens) {
      requireInsecure("Plaintext")
      return stringDeserializer.deserialize(p, ctxt)?.let { ProtectedString(it) }
    }
    return super.deserialize(p, ctxt)
  }

  override fun isSecure(source: ConfigLoaderSource): Boolean =
    when (source) {
      is ConfigLoaderSource.Command -> false // The command source is always unsafe.
      is ConfigLoaderSource.EnvironmentVariable -> false // Sensitive data should not be in environment variables.
      is ConfigLoaderSource.GcpSecret -> true
    }

  override fun convert(string: ProtectedString): ProtectedString =
    string
}
