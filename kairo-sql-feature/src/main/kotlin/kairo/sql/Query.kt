@file:Suppress("ForbiddenMethodCall")

package kairo.sql

import kotlin.reflect.KClass
import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.statement.Query

public inline fun <reified T : Any> Query.mapTo(): ResultIterable<T> =
  mapTo(T::class)

public fun <T : Any> Query.mapTo(kClass: KClass<T>): ResultIterable<T> =
  mapTo(kClass.java)
