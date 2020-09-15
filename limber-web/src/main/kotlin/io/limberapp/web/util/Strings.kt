package io.limberapp.web.util

import kotlin.math.abs

internal fun String.pluralize(count: Int) = when (this) {
  "members" -> pluralize("member", "members", count)
  "permissions" -> pluralize("permission", "permissions", count)
  else -> unknownValue("pluralization target", this)
}

private fun pluralize(singular: String, plural: String, count: Int): String {
  if (abs(count) == 1) return singular
  return plural
}

/**
 * Gets the initials of a name or any other string. For example, "jeff hudson" would become "JH".
 */
internal val String.initials get() = split(' ').mapNotNull { it.firstOrNull()?.toUpperCase() }.joinToString("")

/**
 * Searches the given list of elements for the [query] value. The [searchableSelector] should return a set of strings
 * which are searchable for a given element. The query will be searched on a word-by-word basis, and is not case
 * sensitive.
 */
@Suppress("MagicNumber")
internal fun <T> List<T>.search(query: String, searchableSelector: (T) -> Set<String>): List<T> {
  val queryWords = query.toLowerCase().split(Regex("\\s")).filter { it.isNotEmpty() }
  return this
    .map {
      var score = 0
      val searchableWords = searchableSelector(it).flatMap { it.split(' ').map { it.toLowerCase() } }
      queryWords.forEach { queryWord ->
        when {
          queryWord in searchableWords -> score += 10
          searchableWords.any { it.startsWith(queryWord) } -> score += 4
          searchableWords.any { queryWord in it } -> score += 1
        }
      }
      return@map Pair(it, score)
    }
    .sortedByDescending { it.second }
    .dropLastWhile { it.second <= 0 }
    .map { it.first }
}
