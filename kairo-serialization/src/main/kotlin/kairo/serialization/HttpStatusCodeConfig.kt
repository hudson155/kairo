@file:Suppress("MatchingDeclarationName")

package kairo.serialization

import io.ktor.util.AttributeKey

public enum class HttpStatusCodeFormat(
  internal val serializer: Lazy<HttpStatusCodeSerializer>,
  internal val deserializer: Lazy<HttpStatusCodeDeserializer>,
) {
  Number(
    serializer = lazy { HttpStatusCodeSerializer.AsNumber() },
    deserializer = lazy { HttpStatusCodeDeserializer.AsNumber() },
  ),
  Object(
    serializer = lazy { HttpStatusCodeSerializer.AsObject() },
    deserializer = lazy { HttpStatusCodeDeserializer.AsObject() },
  ),
}

internal data class HttpStatusCodeObjectDelegate(
  val value: Int,
  val description: String,
)

private val key: AttributeKey<HttpStatusCodeFormat> = AttributeKey("httpStatusCodeFormat")

public var KairoJson.Builder.httpStatusCodeFormat: HttpStatusCodeFormat
  get() = attributes.getOrNull(key) ?: HttpStatusCodeFormat.Number
  set(value) {
    attributes[key] = value
  }
