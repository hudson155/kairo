package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.math.BigDecimal

public abstract class BigDecimalSerializer : StdSerializer<BigDecimal>(
  BigDecimal::class.java,
) {
  public class AsDouble : BigDecimalSerializer() {
    override fun serialize(
      value: BigDecimal,
      gen: JsonGenerator,
      provider: SerializerProvider,
    ) {
      gen.writeNumber(value)
    }
  }

  public class AsString : BigDecimalSerializer() {
    override fun serialize(
      value: BigDecimal,
      gen: JsonGenerator,
      provider: SerializerProvider,
    ) {
      val string = value.toPlainString()
      provider.defaultSerializeValue(string, gen)
    }
  }
}
