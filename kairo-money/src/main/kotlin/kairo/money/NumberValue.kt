package kairo.money

import javax.money.NumberValue

public inline fun <reified T : Number> NumberValue.numberValueExact(): T =
  numberValueExact(T::class.javaObjectType)
