package kairo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import kairo.protectedString.ProtectedString
import kairo.serialization.module.primitives.BooleanDeserializer

/**
 * Adds support for [Boolean] from various [ConfigLoaderSource]s.
 */
internal class ConfigBooleanDeserializer(
  config: ConfigLoaderConfig,
) : ConfigDeserializer<Boolean>(Boolean::class, config) {
  private val booleanDeserializer: BooleanDeserializer = BooleanDeserializer()

  /**
   * In addition to the [ConfigLoaderSource]s, booleans can be plaintext.
   */
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Boolean? {
    if (p.currentToken in booleanDeserializer.tokens) {
      return booleanDeserializer.deserialize(p, ctxt)
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
  override fun convert(string: ProtectedString): Boolean =
    string.value.lowercase().toBooleanStrict()
}
