package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.math.BigInteger

public abstract class BigIntegerDeserializer : StdDeserializer<BigInteger>(
  BigInteger::class.java,
) {
  public class AsLong : BigIntegerDeserializer() {
    private val delegate: JsonDeserializer<BigInteger> =
      NumberDeserializers.BigIntegerDeserializer.instance

    override fun deserialize(
      p: JsonParser,
      ctxt: DeserializationContext,
    ): BigInteger =
      delegate.deserialize(p, ctxt)
  }

  public class AsString : BigIntegerDeserializer() {
    override fun deserialize(
      p: JsonParser,
      ctxt: DeserializationContext,
    ): BigInteger {
      val string = p.text
      return BigInteger(string)
    }
  }
}
