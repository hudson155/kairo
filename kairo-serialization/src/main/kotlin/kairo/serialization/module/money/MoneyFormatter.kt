package kairo.serialization.module.money

import java.math.BigDecimal
import java.math.RoundingMode
import kairo.reflect.typeParam
import kairo.serialization.ObjectMapperFactory
import kotlin.math.max
import kotlin.reflect.KClass
import org.javamoney.moneta.Money

public abstract class MoneyFormatter<T : Any> {
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
        amount = amountAsString(money),
        currency = currencyCode(money),
      )
  }

  internal val kClass: KClass<T> = typeParam(MoneyFormatter::class, 0, this::class)

  public abstract fun parse(value: Any): Money

  public abstract fun format(money: Money): T

  protected fun amountAsString(money: Money): String =
    money.number.numberValueExact(BigDecimal::class.java).let { amount ->
      val scale = max(amount.scale(), money.currency.defaultFractionDigits) // Print extra 0s.
      return@let amount.setScale(scale, RoundingMode.UNNECESSARY)
    }.toPlainString()

  protected fun currencyCode(money: Money): String =
    money.currency.currencyCode
}

public var ObjectMapperFactory<*, *>.moneyFormatter: MoneyFormatter<*>
  get() = properties["moneyFormatter"] as MoneyFormatter<*>? ?: MoneyFormatter.Default
  set(value) {
    properties["moneyFormatter"] = value
  }
