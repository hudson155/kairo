package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.module.SimpleModule
import kairo.serialization.ObjectMapperFactoryBuilder
import kotlin.uuid.Uuid

/**
 * Although this is named "primitives", it doesn't just handle primitives.
 * For example, [String] and [Uuid] are not primitives.
 *
 * Regarding UUIDs, Jackson supports [java.util.UUID] by default, but not [kotlin.uuid.Uuid] which we use.
 */
internal class PrimitivesModule(
  builder: ObjectMapperFactoryBuilder,
) : SimpleModule() {
  init {
    configureBoolean()
    configureDouble()
    configureFloat()
    configureInt()
    configureLong()
    configureString(builder)
    configureUuid()
  }

  private fun configureBoolean() {
    BooleanSerializer().let { serializer ->
      addSerializer(Boolean::class.javaPrimitiveType, serializer)
      addSerializer(Boolean::class.javaObjectType, serializer)
    }
    BooleanDeserializer().let { deserializer ->
      addDeserializer(Boolean::class.javaPrimitiveType, deserializer)
      addDeserializer(Boolean::class.javaObjectType, deserializer)
    }
  }

  private fun configureDouble() {
    DoubleSerializer().let { serializer ->
      addSerializer(Double::class.javaPrimitiveType, serializer)
      addSerializer(Double::class.javaObjectType, serializer)
    }
    DoubleDeserializer().let { deserializer ->
      addDeserializer(Double::class.javaPrimitiveType, deserializer)
      addDeserializer(Double::class.javaObjectType, deserializer)
    }
  }

  private fun configureFloat() {
    FloatSerializer().let { serializer ->
      addSerializer(Float::class.javaPrimitiveType, serializer)
      addSerializer(Float::class.javaObjectType, serializer)
    }
    FloatDeserializer().let { deserializer ->
      addDeserializer(Float::class.javaPrimitiveType, deserializer)
      addDeserializer(Float::class.javaObjectType, deserializer)
    }
  }

  private fun configureInt() {
    IntSerializer().let { serializer ->
      addSerializer(Int::class.javaPrimitiveType, serializer)
      addSerializer(Int::class.javaObjectType, serializer)
    }
    IntDeserializer().let { deserializer ->
      addDeserializer(Int::class.javaPrimitiveType, deserializer)
      addDeserializer(Int::class.javaObjectType, deserializer)
    }
  }

  private fun configureLong() {
    LongSerializer().let { serializer ->
      addSerializer(Long::class.javaPrimitiveType, serializer)
      addSerializer(Long::class.javaObjectType, serializer)
    }
    LongDeserializer().let { deserializer ->
      addDeserializer(Long::class.javaPrimitiveType, deserializer)
      addDeserializer(Long::class.javaObjectType, deserializer)
    }
  }

  private fun configureString(builder: ObjectMapperFactoryBuilder) {
    addSerializer(String::class.javaObjectType, StringSerializer())
    addDeserializer(String::class.javaObjectType, StringDeserializer(trimWhitespace = builder.trimWhitespace))
  }

  private fun configureUuid() {
    addSerializer(Uuid::class.javaObjectType, UuidSerializer())
    addDeserializer(Uuid::class.javaObjectType, UuidDeserializer())
  }
}

internal fun ObjectMapperFactoryBuilder.configurePrimitives(builder: ObjectMapperFactoryBuilder) {
  addModule(PrimitivesModule(builder))
}
