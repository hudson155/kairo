package kairo.serialization.module.money

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max
import org.javamoney.moneta.Money

internal data class MoneyIntermediary private constructor(
  val amount: String,
  val currency: String,
) {
  internal companion object {
    fun from(money: Money): MoneyIntermediary =
      MoneyIntermediary(
        amount = money.number.numberValueExact(BigDecimal::class.java).let { amount ->
          val scale = max(amount.scale(), money.currency.defaultFractionDigits) // Print extra 0s.
          return@let amount.setScale(scale, RoundingMode.UNNECESSARY)
        }.toPlainString(),
        currency = money.currency.currencyCode,
      )
  }
}
