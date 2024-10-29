package kairo.serialization.module.money

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kairo.reflect.typeParam
import kairo.serialization.ObjectMapperFactory
import kotlin.math.max
import kotlin.reflect.KClass
import org.javamoney.moneta.Money

public abstract class MoneyFormatter<T : Any> {
  internal val kClass: KClass<T> = typeParam(MoneyFormatter::class, 0, this::class)

  internal abstract fun parse(value: Any): Money

  internal abstract fun format(money: Money): T

  /**
   * This is called "ambiguous string" because there's no way to deserialize it
   * since it doesn't contain the currency code.
   * It should be used with caution.
   */
  public object AmbiguousString : MoneyFormatter<String>() {
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

  /**
   * This is a good standard format to serialize money.
   * It includes currency information and does not lose precision.
   */
  public object Default : MoneyFormatter<Default.Intermediary>() {
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
        amount = money.number.numberValueExact(BigDecimal::class.java).let { amount ->
          val scale = max(amount.scale(), money.currency.defaultFractionDigits) // Print extra 0s.
          return@let amount.setScale(scale, RoundingMode.UNNECESSARY)
        }.toPlainString(),
        currency = money.currency.currencyCode,
      )
  }
}

public var ObjectMapperFactory<*, *>.moneyFormatter: MoneyFormatter<*>
  get() = properties["moneyFormatter"] as MoneyFormatter<*>? ?: MoneyFormatter.Default
  set(value) {
    properties["moneyFormatter"] = value
  }
