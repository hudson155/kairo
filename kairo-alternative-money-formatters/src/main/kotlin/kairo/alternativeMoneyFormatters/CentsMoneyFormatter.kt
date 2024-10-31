package kairo.alternativeMoneyFormatters

import java.math.BigDecimal
import javax.money.CurrencyUnit
import javax.money.Monetary
import kairo.serialization.module.money.MoneyFormatter
import org.javamoney.moneta.Money

/**
 * Serializes only the amount, to a long.
 * Even though it's called "cents", that's not the correct term in all currencies.
 * Moneta calls it the "minor unit".
 *
 * There's no way to deserialize it since it doesn't contain the currency code.
 * Also, it doesn't support sub-minor amounts such as half a cent in USD.
 * Deserialization relies on [currency],
 * since currency information is missing from the serialized representation.
 * Should be used with caution.
 */
public class CentsMoneyFormatter(
  currencyCode: String,
) : MoneyFormatter<Long>() {
  private val currency: CurrencyUnit = Monetary.getCurrency(currencyCode)

  override fun parse(value: Any): Money {
    value as Long
    return Money.ofMinor(currency, value)
  }

  override fun format(money: Money): Long {
    check(money.currency == currency)
    return money.number.numberValueExact(BigDecimal::class.java)
      .multiply(BigDecimal.TEN.pow(currency.defaultFractionDigits))
      .longValueExact()
  }
}
