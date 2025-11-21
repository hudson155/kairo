package kairo.money

import kotlinx.serialization.modules.SerializersModule
import org.javamoney.moneta.Money

public fun moneyModule(): SerializersModule =
  SerializersModule {
    contextual(Money::class) {
      MoneySerializer.Default()
    }
  }
