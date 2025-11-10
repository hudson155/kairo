package kairo.money

import javax.money.Monetary
import org.javamoney.moneta.Money

public fun Money.round(): Money =
  with(Monetary.getDefaultRounding())
