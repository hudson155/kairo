package com.piperframework.store

import org.jdbi.v3.core.statement.SqlStatement

@Suppress("UnnecessaryAbstractClass")
abstract class QueryBuilder {
  val conditions = mutableListOf<String>()
  val bindings = mutableMapOf<String, Any>()
}

fun <S : SqlStatement<S>> S.withFinder(query: QueryBuilder): S {
  return this
    .define("conditions", query.conditions.joinToString(" AND "))
    .bindMap(query.bindings)
}
