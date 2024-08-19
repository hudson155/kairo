@file:Suppress("ForbiddenImport")

package kairo.dependencyInjection

import com.google.inject.TypeLiteral

public inline fun <reified T> type(): TypeLiteral<T> =
  object : TypeLiteral<T>() {}
