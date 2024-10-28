package kairo.serialization.module.primitives

internal class DefaultPrimitivesModule(
  trimWhitespace: TrimWhitespace.Type,
) : PrimitivesModule(trimWhitespace) {
  override fun configureBoolean() {
    BooleanSerializer().let { serializer ->
      addSerializer(Boolean::class.javaPrimitiveType, serializer)
      addSerializer(Boolean::class.javaObjectType, serializer)
    }
    BooleanSerializer.Key().let { serializer ->
      addKeySerializer(Boolean::class.javaPrimitiveType, serializer)
      addKeySerializer(Boolean::class.javaObjectType, serializer)
    }
    BooleanDeserializer().let { deserializer ->
      addDeserializer(Boolean::class.javaPrimitiveType, deserializer)
      addDeserializer(Boolean::class.javaObjectType, deserializer)
    }
    BooleanDeserializer.Key().let { deserializer ->
      addKeyDeserializer(Boolean::class.javaPrimitiveType, deserializer)
      addKeyDeserializer(Boolean::class.javaObjectType, deserializer)
    }
  }

  override fun configureDouble() {
    DoubleSerializer().let { serializer ->
      addSerializer(Double::class.javaPrimitiveType, serializer)
      addSerializer(Double::class.javaObjectType, serializer)
    }
    DoubleSerializer.Key().let { serializer ->
      addKeySerializer(Double::class.javaPrimitiveType, serializer)
      addKeySerializer(Double::class.javaObjectType, serializer)
    }
    DoubleDeserializer().let { deserializer ->
      addDeserializer(Double::class.javaPrimitiveType, deserializer)
      addDeserializer(Double::class.javaObjectType, deserializer)
    }
    DoubleDeserializer.Key().let { deserializer ->
      addKeyDeserializer(Double::class.javaPrimitiveType, deserializer)
      addKeyDeserializer(Double::class.javaObjectType, deserializer)
    }
  }

  override fun configureFloat() {
    FloatSerializer().let { serializer ->
      addSerializer(Float::class.javaPrimitiveType, serializer)
      addSerializer(Float::class.javaObjectType, serializer)
    }
    FloatSerializer.Key().let { serializer ->
      addKeySerializer(Float::class.javaPrimitiveType, serializer)
      addKeySerializer(Float::class.javaObjectType, serializer)
    }
    FloatDeserializer().let { deserializer ->
      addDeserializer(Float::class.javaPrimitiveType, deserializer)
      addDeserializer(Float::class.javaObjectType, deserializer)
    }
    FloatDeserializer.Key().let { deserializer ->
      addKeyDeserializer(Float::class.javaPrimitiveType, deserializer)
      addKeyDeserializer(Float::class.javaObjectType, deserializer)
    }
  }

  override fun configureInt() {
    IntSerializer().let { serializer ->
      addSerializer(Int::class.javaPrimitiveType, serializer)
      addSerializer(Int::class.javaObjectType, serializer)
    }
    IntSerializer.Key().let { serializer ->
      addKeySerializer(Int::class.javaPrimitiveType, serializer)
      addKeySerializer(Int::class.javaObjectType, serializer)
    }
    IntDeserializer().let { deserializer ->
      addDeserializer(Int::class.javaPrimitiveType, deserializer)
      addDeserializer(Int::class.javaObjectType, deserializer)
    }
    IntDeserializer.Key().let { deserializer ->
      addKeyDeserializer(Int::class.javaPrimitiveType, deserializer)
      addKeyDeserializer(Int::class.javaObjectType, deserializer)
    }
  }

  override fun configureLong() {
    LongSerializer().let { serializer ->
      addSerializer(Long::class.javaPrimitiveType, serializer)
      addSerializer(Long::class.javaObjectType, serializer)
    }
    LongSerializer.Key().let { serializer ->
      addKeySerializer(Long::class.javaPrimitiveType, serializer)
      addKeySerializer(Long::class.javaObjectType, serializer)
    }
    LongDeserializer().let { deserializer ->
      addDeserializer(Long::class.javaPrimitiveType, deserializer)
      addDeserializer(Long::class.javaObjectType, deserializer)
    }
    LongDeserializer.Key().let { deserializer ->
      addKeyDeserializer(Long::class.javaPrimitiveType, deserializer)
      addKeyDeserializer(Long::class.javaObjectType, deserializer)
    }
  }
}