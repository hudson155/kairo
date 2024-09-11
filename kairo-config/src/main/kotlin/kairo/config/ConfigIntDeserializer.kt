package kairo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import kairo.protectedString.ProtectedString
import kairo.serialization.IntDeserializer

/**
 * Adds support for [Int] from various [ConfigLoaderSource]s.
 */
internal class ConfigIntDeserializer(
  config: ConfigLoaderConfig,
) : ConfigDeserializer<Int>(Int::class, config) {
  private val intDeserializer: IntDeserializer = IntDeserializer()

  /**
   * In addition to the [ConfigLoaderSource]s, ints can be plaintext.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Int? {
    if (p.currentToken in intDeserializer.tokens) {
      return intDeserializer.deserialize(p, ctxt)
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
  override fun convert(string: ProtectedString): Int =
    string.value.toInt()
}
