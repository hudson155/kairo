package kairo.serialization

import io.ktor.util.AttributeKey

public enum class BigIntegerFormat(
  internal val serializer: Lazy<BigIntegerSerializer>,
  internal val deserializer: Lazy<BigIntegerDeserializer>,
) {
  Long(
    serializer = lazy { BigIntegerSerializer.AsLong() },
    deserializer = lazy { BigIntegerDeserializer.AsLong() },
  ),
  String(
    serializer = lazy { BigIntegerSerializer.AsString() },
    deserializer = lazy { BigIntegerDeserializer.AsString() },
  ),
}

private val key: AttributeKey<BigIntegerFormat> = AttributeKey("bigIntegerFormat")

public var KairoJson.Builder.bigIntegerFormat: BigIntegerFormat
  get() = attributes.getOrNull(key) ?: BigIntegerFormat.Long
  set(value) {
    attributes[key] = value
  }
