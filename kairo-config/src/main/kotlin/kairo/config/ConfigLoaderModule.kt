package kairo.config

import com.fasterxml.jackson.databind.module.SimpleModule
import kairo.protectedString.ProtectedString

/**
 * A Jackson module that adds support for [ConfigLoaderSource].
 * See the Feature README for more information.
 */
internal class ConfigLoaderModule private constructor(
  private val config: ConfigLoaderConfig,
) : SimpleModule() {
  init {
    configureInt()
    configureProtectedString()
    configureString()
  }

  private fun configureInt() {
    ConfigIntDeserializer(config).let { deserializer ->
      addDeserializer(Int::class.javaPrimitiveType, deserializer)
      addDeserializer(Int::class.javaObjectType, deserializer)
    }
  }

  private fun configureProtectedString() {
    addDeserializer(ProtectedString::class.javaObjectType, ConfigProtectedStringDeserializer(config))
  }

  private fun configureString() {
    addDeserializer(String::class.javaObjectType, ConfigStringDeserializer(config))
  }

  internal companion object {
    fun create(config: ConfigLoaderConfig): ConfigLoaderModule =
      ConfigLoaderModule(
        config = config,
      )
  }
}
