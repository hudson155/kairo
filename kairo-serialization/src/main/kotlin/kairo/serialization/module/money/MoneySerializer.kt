package kairo.serialization.module.money

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.javamoney.moneta.Money

public class MoneySerializer : StdSerializer<Money>(Money::class.java) {
  override fun serialize(value: Money, gen: JsonGenerator, provider: SerializerProvider) {
    val intermediary = MoneyIntermediary.from(value)
    gen.writeObject(intermediary)
  }
}
