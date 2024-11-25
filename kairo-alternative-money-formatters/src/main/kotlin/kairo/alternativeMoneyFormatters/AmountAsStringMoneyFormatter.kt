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
   * During deserialization this formatter will (rightly or wrongly) always assume the value is for that currency.
   */
  currencyCode: String,
) : MoneyFormatter<String>() {
  private val currency: CurrencyUnit = Monetary.getCurrency(currencyCode)

  override fun parse(value: Any): Money {
    value as String
    return Money.of(BigDecimal(value), currency)
  }

  override fun format(money: Money): String {
    check(money.currency == currency)
    return amountAsString(money)
  }
}
