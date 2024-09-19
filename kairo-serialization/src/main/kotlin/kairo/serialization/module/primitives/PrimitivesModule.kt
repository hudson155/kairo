package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.fasterxml.jackson.databind.ser.Serializers
import kairo.serialization.ObjectMapperFactoryBuilder
import kotlin.uuid.Uuid

/**
 * Although this is named "primitives", it doesn't just handle primitives.
 * For example, [String] and [Uuid] are not primitives
 */
internal class PrimitivesModule(
  private val builder: ObjectMapperFactoryBuilder,
) : SimpleModule() {
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
      BooleanDeserializer().let { deserializer ->
        addDeserializer(Boolean::class.javaPrimitiveType, deserializer)
        addDeserializer(Boolean::class.javaObjectType, deserializer)
      }
      DoubleDeserializer().let { deserializer ->
        addDeserializer(Double::class.javaPrimitiveType, deserializer)
        addDeserializer(Double::class.javaObjectType, deserializer)
      }
      FloatDeserializer().let { deserializer ->
        addDeserializer(Float::class.javaPrimitiveType, deserializer)
        addDeserializer(Float::class.javaObjectType, deserializer)
      }
      IntDeserializer().let { deserializer ->
        addDeserializer(Int::class.javaPrimitiveType, deserializer)
        addDeserializer(Int::class.javaObjectType, deserializer)
      }
      LongDeserializer().let { deserializer ->
        addDeserializer(Long::class.javaPrimitiveType, deserializer)
        addDeserializer(Long::class.javaObjectType, deserializer)
      }

      addDeserializer(String::class.javaObjectType, StringDeserializer(trimWhitespace = builder.trimWhitespace))
      addDeserializer(Uuid::class.javaObjectType, UuidDeserializer())
    }
}

internal fun ObjectMapperFactoryBuilder.configurePrimitives(builder: ObjectMapperFactoryBuilder) {
  addModule(PrimitivesModule(builder))
}
