package com.piperframework.finder

interface Finder<Model : Any, Finder : Any> {
  fun <R> find(result: (Iterable<Model>) -> R, query: Finder.() -> Unit): R

  fun findOnlyOrThrow(query: Finder.() -> Unit) = find(Iterable<Model>::single, query)

  fun findOnlyOrNull(query: Finder.() -> Unit) = find(Iterable<Model>::singleNullOrThrow, query)

  fun findAsList(query: Finder.() -> Unit) = find(Iterable<Model>::toList, query)

  fun findAsSet(query: Finder.() -> Unit) = find(Iterable<Model>::toSet, query)

  fun has(query: Finder.() -> Unit) = find(Iterable<Model>::isNotEmpty, query)
}
