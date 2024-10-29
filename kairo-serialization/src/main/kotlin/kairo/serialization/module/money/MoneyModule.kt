package kairo.serialization.module.money

import com.fasterxml.jackson.databind.module.SimpleModule
import org.javamoney.moneta.Money

/**
 * Configures the Kairo-recognized [Money] type.
 */
internal class MoneyModule private constructor() : SimpleModule() {
  init {
    configureMoney()
  }

  private fun configureMoney() {
    addSerializer(Money::class.javaObjectType, MoneySerializer())
    addDeserializer(Money::class.javaObjectType, MoneyDeserializer())
  }

  internal companion object {
    fun create(): MoneyModule =
      MoneyModule()
  }
}
