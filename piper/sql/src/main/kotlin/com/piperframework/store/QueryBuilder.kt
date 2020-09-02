package com.piperframework.store

import org.jdbi.v3.core.statement.SqlStatement

@Suppress("UnnecessaryAbstractClass")
abstract class QueryBuilder {
  val conditions = mutableListOf<String>()
  val bindings = mutableMapOf<String, Any>()
  val sorts = mutableListOf<Pair<String, Boolean>>() // Column name and ascending flag.
}

fun <S : SqlStatement<S>> S.withFinder(query: QueryBuilder): S {
  val conditions = query.conditions.joinToString(" AND ")
  val sorts = query.sorts.let {
    return@let if (it.isNotEmpty()) {
      it.joinToString(", ") { "${it.first} ${if (it.second) "ASC" else "DESC"}" }
    } else {
      "1" // If there are no sorts, using "1" means the rows don't get sorted.
    }
  }
  return this
    .define("conditions", conditions)
    .define("sort", sorts)
    .bindMap(query.bindings)
}
