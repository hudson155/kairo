package kairo.money

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import javax.money.CurrencyUnit

public class CurrencyUnitSerializer : StdSerializer<CurrencyUnit>(
  CurrencyUnit::class.java,
) {
  override fun serialize(
    value: CurrencyUnit,
    gen: JsonGenerator,
    provider: SerializerProvider,
  ) {
    gen.writeString(value.currencyCode)
  }
}
