package kairo.serialization.module.money

import org.javamoney.moneta.Money

/**
 * Serializes only the amount, to a string
 * There's no way to deserialize it since it doesn't contain the currency code.
 * Should be used with caution.
 *
 * This format is compatible with Shopify.
 */
public object AmountAsStringMoneyFormatter : MoneyFormatter<String>() {
  @Suppress("NotImplementedDeclaration")
  override fun parse(value: Any): Money {
    throw NotImplementedError()
  }

  override fun format(money: Money): String =
    amountAsString(money)
}
