package kairo.alternativeMoneyFormatters

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import javax.money.CurrencyUnit
import javax.money.Monetary
import kairo.serialization.module.money.MoneyFormatter
import org.javamoney.moneta.Money

/**
 * This is called "ambiguous string" because there's no way to deserialize it without knowing the currency in advance,
 * since the serialized format doesn't contain the currency code.
 */
public class AmbiguousStringMoneyFormatter(
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
    val format = formatForCurrency(currency)
    return Money.of(format.parse(value.toString()), currency)
  }

  override fun format(money: Money): String {
    if (currency != null) check(money.currency == currency)
    val format = formatForCurrency(money.currency)
    return format.format(money.number.numberValueExact(BigDecimal::class.java))
  }

  private fun formatForCurrency(currency: CurrencyUnit): NumberFormat =
    NumberFormat.getCurrencyInstance(Locale.US).apply {
      maximumFractionDigits = Int.MAX_VALUE
      minimumFractionDigits = currency.defaultFractionDigits
      this.currency = Currency.getInstance(currency.currencyCode)
      roundingMode = RoundingMode.UNNECESSARY
    }
}
