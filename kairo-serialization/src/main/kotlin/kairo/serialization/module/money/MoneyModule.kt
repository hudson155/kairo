package kairo.serialization.module.money

import com.fasterxml.jackson.databind.module.SimpleModule
import kairo.serialization.ObjectMapperFactory
import org.javamoney.moneta.Money

/**
 * Configures the Kairo-recognized [Money] type.
 */
internal class MoneyModule private constructor(
  private val moneyFormatter: MoneyFormatter<*>,
) : SimpleModule() {
  init {
    configureMoney()
  }

  private fun configureMoney() {
    addSerializer(Money::class.javaObjectType, MoneySerializer(moneyFormatter = moneyFormatter))
    addDeserializer(Money::class.javaObjectType, MoneyDeserializer(moneyFormatter = moneyFormatter))
  }

  internal companion object {
    fun create(factory: ObjectMapperFactory<*, *>): MoneyModule =
      MoneyModule(
        moneyFormatter = factory.moneyFormatter,
      )
  }
}
