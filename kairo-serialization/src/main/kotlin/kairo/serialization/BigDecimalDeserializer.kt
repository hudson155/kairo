package kairo.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.math.BigDecimal

public abstract class BigDecimalDeserializer : StdDeserializer<BigDecimal>(
  BigDecimal::class.java,
) {
  public class AsDouble : BigDecimalDeserializer() {
    private val delegate: JsonDeserializer<BigDecimal> =
      NumberDeserializers.BigDecimalDeserializer.instance

    override fun deserialize(
      p: JsonParser,
      ctxt: DeserializationContext,
    ): BigDecimal =
      delegate.deserialize(p, ctxt)
  }

  public class AsString : BigDecimalDeserializer() {
    override fun deserialize(
      p: JsonParser,
      ctxt: DeserializationContext,
    ): BigDecimal {
      val string = p.text
      return BigDecimal(string)
    }
  }
}
