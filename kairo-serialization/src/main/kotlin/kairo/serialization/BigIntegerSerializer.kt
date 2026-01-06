package kairo.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.math.BigInteger

public abstract class BigIntegerSerializer : StdSerializer<BigInteger>(
  BigInteger::class.java,
) {
  public class AsLong : BigIntegerSerializer() {
    override fun serialize(
      value: BigInteger,
      gen: JsonGenerator,
      provider: SerializerProvider,
    ) {
      gen.writeNumber(value)
    }
  }

  public class AsString : BigIntegerSerializer() {
    override fun serialize(
      value: BigInteger,
      gen: JsonGenerator,
      provider: SerializerProvider,
    ) {
      val string = value.toString()
      provider.defaultSerializeValue(string, gen)
    }
  }
}
