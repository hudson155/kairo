package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

public class BooleanSerializer : StdSerializer<Boolean>(Boolean::class.java) {
  override fun serialize(value: Boolean, gen: JsonGenerator, provider: SerializerProvider) {
    gen.writeBoolean(value)
  }
}
