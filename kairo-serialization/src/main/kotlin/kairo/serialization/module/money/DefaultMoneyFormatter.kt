package kairo.serialization.module.money

import java.math.BigDecimal
import org.javamoney.moneta.Money

/**
 * This is a good standard format to serialize money.
 * It includes currency information and does not lose precision.
 */
public object DefaultMoneyFormatter : MoneyFormatter<DefaultMoneyFormatter.Intermediary>() {
  public data class Intermediary(
    val amount: String,
    val currency: String,
  )

  override fun parse(value: Any): Money {
    value as Intermediary
    return Money.of(BigDecimal(value.amount), value.currency)
  }

  override fun format(money: Money): Intermediary =
    Intermediary(
      amount = amountAsString(money),
      currency = currencyCode(money),
    )
}
