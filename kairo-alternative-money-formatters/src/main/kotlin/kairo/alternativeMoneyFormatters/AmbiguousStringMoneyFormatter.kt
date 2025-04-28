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
   * During deserialization this formatter will (rightly or wrongly) always assume the value is for that currency.
   */
  currencyCode: String,
) : MoneyFormatter<String>() {
  private val currency: CurrencyUnit = Monetary.getCurrency(currencyCode)

  override fun parse(value: Any): Money {
    value as String
    val format = formatForCurrency(currency)
    return Money.of(format.parse(value.toString()), currency)
  }

  override fun format(money: Money): String {
    check(money.currency == currency)
    val format = formatForCurrency(money.currency)
    return format.format(money.number.numberValueExact(BigDecimal::class.java))
  }

  private fun formatForCurrency(currency: CurrencyUnit): NumberFormat =
    NumberFormat.getCurrencyInstance(Locale.US).apply {
      this.maximumFractionDigits = Int.MAX_VALUE
      this.minimumFractionDigits = currency.defaultFractionDigits
      this.currency = Currency.getInstance(currency.currencyCode)
      this.roundingMode = RoundingMode.UNNECESSARY
    }
}
