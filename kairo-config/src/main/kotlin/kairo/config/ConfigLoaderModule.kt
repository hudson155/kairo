package kairo.config

import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import kairo.protectedString.ProtectedString

/**
 * A Jackson module that adds support for [ConfigLoaderSource].
 * See the Feature README for more information.
 */
internal class ConfigLoaderModule(
  private val config: ConfigLoaderConfig,
) : SimpleModule() {
  override fun setupModule(context: SetupContext) {
    super.setupModule(context)
    context.addDeserializers(buildDeserializers())
  }

  private fun buildDeserializers(): Deserializers =
    SimpleDeserializers().apply {
      addDeserializer(ProtectedString::class.javaObjectType, ConfigProtectedStringDeserializer(config))
      addDeserializer(String::class.javaObjectType, ConfigStringDeserializer(config))
    }
}
