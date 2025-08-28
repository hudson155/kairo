package kairo.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull

/**
 * Adapted from [single] and [singleOrNull].
 * Returns the single element, or null if the collection is empty.
 * If the collection has more than one element, throws [IllegalArgumentException].
 *
 * This is less surprising than [singleOrNull]
 * because it doesn't return null when there are multiple items in the collection.
 */
public suspend fun <T> Flow<T>.singleNullOrThrow(): T? {
  var single: T? = null
  var found = false
  collect { element ->
    require(!found) { "Collection has more than one element." }
    single = element
    found = true
  }
  if (!found) return null
  return single
}

/**
 * Adapted from [single] and [singleOrNull].
 * Returns the single element matching the given [predicate], or null if no elements match.
 * If more than one element matches, throws [IllegalArgumentException].
 *
 * This is less surprising than [singleOrNull]
 * because it doesn't return null when there are multiple items in the collection.
 */
public suspend inline fun <T> Flow<T>.singleNullOrThrow(crossinline predicate: (T) -> Boolean): T? {
  var single: T? = null
  var found = false
  collect { element ->
    if (!predicate(element)) return@collect
    require(!found) { "Predicate matches more than one element." }
    single = element
    found = true
  }
  if (!found) return null
  return single
}
