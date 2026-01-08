package kairo.money

import com.fasterxml.jackson.databind.module.SimpleModule
import javax.money.CurrencyUnit
import kairo.serialization.KairoJson
import org.javamoney.moneta.Money

public class MoneyModule(
  builder: KairoJson.Builder,
) : SimpleModule() {
  init {
    addSerializer(CurrencyUnit::class.java, CurrencyUnitSerializer())
    addDeserializer(CurrencyUnit::class.java, CurrencyUnitDeserializer())

    addSerializer(Money::class.java, builder.moneyFormat.serializer.value)
    addDeserializer(Money::class.java, builder.moneyFormat.deserializer.value)
  }
}

public fun KairoJson.Builder.MoneyModule(): MoneyModule =
  MoneyModule(this)
