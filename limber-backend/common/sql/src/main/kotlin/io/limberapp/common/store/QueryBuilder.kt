package io.limberapp.common.store

import io.limberapp.common.store.QueryBuilder.Bindings.Companion.bind
import org.jdbi.v3.core.statement.SqlStatement
import kotlin.reflect.KType
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

abstract class QueryBuilder {
  class Conditions {
    private val conditions = mutableListOf<String>()

    operator fun plusAssign(condition: String) {
      conditions += condition
    }

    override fun toString() = conditions.joinToString(" AND ")
  }

  class Bindings {
    private val bindings = mutableMapOf<String, Pair<Any, KType>>()

    inline operator fun <reified T : Any> set(name: String, value: T) {
      this[name] = value to typeOf<T>()
    }

    operator fun set(name: String, value: Pair<Any, KType>) {
      bindings[name] = value
    }

    companion object {
      fun <This : SqlStatement<This>> SqlStatement<This>.bind(bindings: Bindings): This {
        bindings.bindings.forEach { (key, value) ->
          bindByType(key, value.first, value.second.javaType)
        }
        return this as This
      }
    }
  }

  class Sorts {
    private val sorts = mutableListOf<Pair<String, Boolean>>() // Column name and ascending flag.

    operator fun plusAssign(sort: Pair<String, Boolean>) {
      sorts += sort
    }

    override fun toString() = sorts.let {
      return@let if (it.isNotEmpty()) {
        it.joinToString(", ") { "${it.first} ${if (it.second) "ASC" else "DESC"}" }
      } else {
        "1" // If there are no sorts, using "1" means the rows don't get sorted.
      }
    }
  }

  val conditions = Conditions()

  val bindings = Bindings()

  val sorts = Sorts()
}

fun <S : SqlStatement<S>> S.withFinder(query: QueryBuilder): S = this
  .define("conditions", query.conditions.toString())
  .define("sort", query.sorts.toString())
  .bind(query.bindings)
