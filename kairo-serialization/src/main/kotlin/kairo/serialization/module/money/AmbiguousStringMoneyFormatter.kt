package kairo.serialization.module.money

import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import org.javamoney.moneta.Money

/**
 * This is called "ambiguous string" because there's no way to deserialize it
 * since it doesn't contain the currency code.
 * It should be used with caution.
 *
 * This format is compatible with Google Sheets.
 */
public object AmbiguousStringMoneyFormatter : MoneyFormatter<String>() {
  @Suppress("NotImplementedDeclaration")
  override fun parse(value: Any): Money {
    throw NotImplementedError()
  }

  override fun format(money: Money): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US).apply {
      maximumFractionDigits = Int.MAX_VALUE
      minimumFractionDigits = money.currency.defaultFractionDigits
      currency = Currency.getInstance(money.currency.currencyCode)
      roundingMode = RoundingMode.UNNECESSARY
    }
    return format.format(money.numberStripped)
  }
}
