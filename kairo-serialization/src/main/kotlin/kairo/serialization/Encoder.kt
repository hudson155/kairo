package kairo.serialization

import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

public inline fun <reified T> Encoder.encodeSerializableValue(value: T) {
  val serializer = serializersModule.serializer<T>()
  encodeSerializableValue(serializer, value)
}
