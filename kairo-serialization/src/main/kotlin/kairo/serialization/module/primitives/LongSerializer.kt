package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

public class LongSerializer : StdSerializer<Long>(Long::class.java) {
  public class Key : StdSerializer<Long>(Long::class.java) {
    override fun serialize(value: Long, gen: JsonGenerator, provider: SerializerProvider) {
      val string = convert(value)
      gen.writeFieldName(string)
    }
  }

  override fun serialize(value: Long, gen: JsonGenerator, provider: SerializerProvider) {
    gen.writeNumber(value)
  }

  public companion object {
    private fun convert(value: Long): String =
      value.toString()
  }
}
