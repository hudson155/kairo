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
 * There's no way to deserialize it without knowing the currency in advance,
 * since the serialized format doesn't contain the currency code.
 * Also, it doesn't support sub-minor amounts such as half a cent in USD.
 */
public class CentsMoneyFormatter(
  /**
   * Providing a non-null [currencyCode] enables deserialization for the given currency.
   * During deserialization this formatter will (rightly or wrongly) always assume the value is for that currency.
   *
   * Leaving [currencyCode] null means deserialization won't work at all.
   */
  currencyCode: String? = null,
) : MoneyFormatter<Long>() {
  private val currency: CurrencyUnit? = currencyCode?.let { Monetary.getCurrency(it) }

  override fun parse(value: Any): Money {
    currency ?: throw UnsupportedOperationException()
    value as Long
    return Money.ofMinor(currency, value)
  }

  override fun format(money: Money): Long {
    if (currency != null) check(money.currency == currency)
    return money.number.numberValueExact(BigDecimal::class.java)
      .multiply(BigDecimal.TEN.pow(money.currency.defaultFractionDigits))
      .longValueExact()
  }
}
