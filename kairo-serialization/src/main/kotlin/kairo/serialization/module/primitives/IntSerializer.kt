package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

public class IntSerializer : StdSerializer<Int>(Int::class.java) {
  override fun serialize(value: Int, gen: JsonGenerator, provider: SerializerProvider) {
    gen.writeNumber(value)
  }
}
