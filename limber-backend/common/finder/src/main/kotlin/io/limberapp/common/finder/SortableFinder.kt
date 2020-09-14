package io.limberapp.common.finder

interface SortableFinder<SortBy : Any> {
  enum class SortDirection { ASCENDING, DESCENDING }

  fun sortBy(sortBy: SortBy, sortDirection: SortDirection)
}
