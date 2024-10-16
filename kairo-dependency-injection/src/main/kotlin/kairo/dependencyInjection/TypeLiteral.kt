@file:Suppress("ForbiddenImport")

package kairo.dependencyInjection

import com.google.inject.TypeLiteral

public inline fun <reified T : Any> type(): TypeLiteral<T> =
  object : TypeLiteral<T>() {}
