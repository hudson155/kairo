package kairo.serialization

import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.fasterxml.jackson.databind.ser.Serializers
import kotlin.uuid.Uuid

/**
 * Jackson supports [java.util.UUID] by default, but not [kotlin.uuid.Uuid] which we use.
 */
internal class UuidModule : SimpleModule() {
  override fun setupModule(context: SetupContext) {
    super.setupModule(context)
    context.addSerializers(buildSerializers())
    context.addDeserializers(buildDeserializers())
  }

  private fun buildSerializers(): Serializers =
    SimpleSerializers().apply {
      addSerializer(Uuid::class.javaObjectType, UuidSerializer())
    }

  private fun buildDeserializers(): Deserializers =
    SimpleDeserializers().apply {
      addDeserializer(Uuid::class.javaObjectType, UuidDeserializer())
    }
}

internal fun ObjectMapperFactoryBuilder.configureUuids() {
  addModule(UuidModule())
}
