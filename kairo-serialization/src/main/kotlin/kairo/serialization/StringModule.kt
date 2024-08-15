package kairo.serialization

import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule

internal class StringModule : SimpleModule() {
  override fun setupModule(context: SetupContext) {
    super.setupModule(context)
    context.addDeserializers(buildDeserializers())
  }

  private fun buildDeserializers(): Deserializers =
    SimpleDeserializers().apply {
      addDeserializer(String::class.javaObjectType, StringDeserializer())
    }
}

internal fun ObjectMapperFactoryBuilder.configureStrings() {
  addModule(StringModule())
}
