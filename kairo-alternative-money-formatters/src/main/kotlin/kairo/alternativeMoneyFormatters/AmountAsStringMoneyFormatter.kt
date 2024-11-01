package kairo.alternativeMoneyFormatters

import java.math.BigDecimal
import javax.money.CurrencyUnit
import javax.money.Monetary
import kairo.serialization.module.money.MoneyFormatter
import org.javamoney.moneta.Money

/**
 * Serializes only the amount, to a string.
 * There's no way to deserialize it without knowing the currency in advance,
 * since the serialized format doesn't contain the currency code.
 */
public class AmountAsStringMoneyFormatter(
  /**
   * Providing a non-null [currencyCode] enables deserialization for the given currency.
   * During deserialization this formatter will (rightly or wrongly) always assume the value is for that currency.
   *
   * Leaving [currencyCode] null means deserialization won't work at all.
   */
  currencyCode: String? = null,
) : MoneyFormatter<String>() {
  private val currency: CurrencyUnit? = currencyCode?.let { Monetary.getCurrency(it) }

  override fun parse(value: Any): Money {
    currency ?: throw UnsupportedOperationException()
    value as String
    return Money.of(BigDecimal(value), currency)
  }

  override fun format(money: Money): String {
    if (currency != null) check(money.currency == currency)
    return amountAsString(money)
  }
}
