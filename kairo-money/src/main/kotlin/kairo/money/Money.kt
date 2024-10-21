package kairo.money

import javax.money.Monetary
import org.javamoney.moneta.Money

public fun Money.round(): Money =
  with(Monetary.getDefaultRounding())

public fun Iterable<Money>.sumOrNull(): Money? =
  reduceOrNull(Money::add)

public fun Iterable<Money>.sum(currencyCode: String): Money =
  fold(Money.of(0, currencyCode), Money::add)
