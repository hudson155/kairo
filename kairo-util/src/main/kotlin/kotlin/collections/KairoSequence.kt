@file:Suppress("InvalidPackageDeclaration")

package kotlin.collections

/**
 * Adapted from [Sequence.single] and [Sequence.singleOrNull].
 * Returns the single element, or null if the collection is empty.
 * If the collection has more than one element, throws [IllegalArgumentException].
 *
 * This is less surprising than [Sequence.singleOrNull]
 * because it doesn't return null when there are multiple items in the collection.
 */
public fun <T> Sequence<T>.singleNullOrThrow(): T? {
  val iterator = iterator()
  if (!iterator.hasNext()) return null
  val single = iterator.next()
  if (!iterator.hasNext()) return single
  throw IllegalArgumentException("Collection has more than one element.")
}

/**
 * Adapted from [Sequence.single] and [Sequence.singleOrNull].
 * Returns the single element matching the given [predicate], or null if no elements match.
 * If more than one element matches, throws [IllegalArgumentException].
 *
 * This is less surprising than [Sequence.singleOrNull]
 * because it doesn't return null when there are multiple items in the collection.
 */
public inline fun <T> Sequence<T>.singleNullOrThrow(predicate: (T) -> Boolean): T? {
  var single: T? = null
  var found = false
  for (element in this) {
    if (!predicate(element)) continue
    require(!found) { "Predicate matches more than one element." }
    single = element
    found = true
  }
  if (!found) return null
  return single
}
