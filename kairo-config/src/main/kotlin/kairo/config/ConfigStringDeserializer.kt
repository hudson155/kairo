package kairo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import kairo.protectedString.ProtectedString
import kairo.serialization.StringDeserializer

internal class ConfigStringDeserializer(
  config: ConfigLoaderConfig,
) : ConfigDeserializer<String>(String::class, config) {
  private val stringDeserializer: StringDeserializer = StringDeserializer()

  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String? {
    if (p.currentToken in stringDeserializer.tokens) {
      return stringDeserializer.deserialize(p, ctxt)
    }
    return super.deserialize(p, ctxt)
  }

  override fun isSecure(source: ConfigLoaderSource): Boolean =
    when (source) {
      is ConfigLoaderSource.Command -> false
      is ConfigLoaderSource.EnvironmentVariable -> true
      is ConfigLoaderSource.GcpSecret -> false
    }

  @OptIn(ProtectedString.Access::class)
  override fun convert(string: ProtectedString): String =
    string.value
}
