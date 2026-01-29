package kairo.money

import com.fasterxml.jackson.databind.module.SimpleModule
import javax.money.CurrencyUnit
import org.javamoney.moneta.Money

public class MoneyModule internal constructor(
  moneyFormat: MoneyFormat,
) : SimpleModule() {
  public class Builder {
    public var moneyFormat: MoneyFormat = MoneyFormat.Default
  }

  init {
    addSerializer(CurrencyUnit::class.java, CurrencyUnitSerializer())
    addDeserializer(CurrencyUnit::class.java, CurrencyUnitDeserializer())

    addSerializer(Money::class.java, moneyFormat.serializer)
    addDeserializer(Money::class.java, moneyFormat.deserializer)
  }
}

public fun MoneyModule(
  block: MoneyModule.Builder.() -> Unit = {},
): MoneyModule {
  val builder = MoneyModule.Builder().apply(block)
  return MoneyModule(builder.moneyFormat)
}
