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

  @Suppress("ComplexRedundantLet", "UnnecessaryLet")
  private fun buildDeserializers(): Deserializers =
    SimpleDeserializers().apply {
      ConfigIntDeserializer(config).let { deserializer ->
        addDeserializer(Int::class.javaPrimitiveType, deserializer)
        addDeserializer(Int::class.javaObjectType, deserializer)
      }
      ConfigProtectedStringDeserializer(config).let { deserializer ->
        addDeserializer(ProtectedString::class.javaObjectType, deserializer)
      }
      ConfigStringDeserializer(config).let { deserializer ->
        addDeserializer(String::class.javaObjectType, deserializer)
      }
    }
}
