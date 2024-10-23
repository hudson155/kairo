package kairo.serialization.module.primitives

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

public class DoubleSerializer : StdSerializer<Double>(Double::class.java) {
  public class Key : StdSerializer<Double>(Double::class.java) {
    override fun serialize(value: Double, gen: JsonGenerator, provider: SerializerProvider) {
      val string = convert(value)
      gen.writeFieldName(string)
    }
  }

  override fun serialize(value: Double, gen: JsonGenerator, provider: SerializerProvider) {
    gen.writeNumber(value)
  }

  public companion object {
    @JvmStatic
    private fun convert(value: Double): String =
      value.toString()
  }
}
