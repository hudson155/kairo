package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

public class StringSerializer : StdSerializer<String>(String::class.java) {
  override fun serialize(value: String, gen: JsonGenerator, provider: SerializerProvider) {
    gen.writeString(value)
  }
}
