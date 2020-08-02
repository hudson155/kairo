package com.piperframework.finder

interface Finder<Model : Any, Finder : Any> {
  fun <R> find(result: (Iterable<Model>) -> R, query: Finder.() -> Unit): R

  fun findOnlyOrThrow(query: Finder.() -> Unit) = find(Iterable<Model>::single, query)

  fun findOnlyOrNull(query: Finder.() -> Unit) = find(Iterable<Model>::singleNullOrThrow, query)

  fun findAsSet(query: Finder.() -> Unit) = find(Iterable<Model>::toSet, query)
}
