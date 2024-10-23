package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

public class FloatSerializer : StdSerializer<Float>(Float::class.java) {
  public class Key : StdSerializer<Float>(Float::class.java) {
    override fun serialize(value: Float, gen: JsonGenerator, provider: SerializerProvider) {
      val string = convert(value)
      gen.writeFieldName(string)
    }
  }

  override fun serialize(value: Float, gen: JsonGenerator, provider: SerializerProvider) {
    gen.writeNumber(value)
  }

  public companion object {
    @JvmStatic
    private fun convert(value: Float): String =
      value.toString()
  }
}
