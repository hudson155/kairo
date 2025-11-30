package kairo.serialization

import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.serializer

public inline fun <reified T> Decoder.decodeSerializableValue(): T {
  val serializer = serializersModule.serializer<T>()
  return decodeSerializableValue(serializer)
}
