package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

public class FloatSerializer : StdSerializer<Float>(Float::class.java) {
  override fun serialize(value: Float, gen: JsonGenerator, provider: SerializerProvider) {
    gen.writeNumber(value)
  }
}
