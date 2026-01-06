@file:Suppress("MatchingDeclarationName")

package kairo.serialization

import io.ktor.util.AttributeKey

public enum class BigDecimalFormat(
  internal val serializer: Lazy<BigDecimalSerializer>,
  internal val deserializer: Lazy<BigDecimalDeserializer>,
) {
  Double(
    serializer = lazy { BigDecimalSerializer.AsDouble() },
    deserializer = lazy { BigDecimalDeserializer.AsDouble() },
  ),
  String(
    serializer = lazy { BigDecimalSerializer.AsString() },
    deserializer = lazy { BigDecimalDeserializer.AsString() },
  ),
}

private val key: AttributeKey<BigDecimalFormat> = AttributeKey("bigDecimalFormat")

public var KairoJson.Builder.bigDecimalFormat: BigDecimalFormat
  get() = attributes.getOrNull(key) ?: BigDecimalFormat.Double
  set(value) {
    attributes[key] = value
  }
