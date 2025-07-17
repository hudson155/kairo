@file:Suppress("InvalidPackageDeclaration", "MatchingDeclarationName")

package kotlin.collections

/**
 * Adapted from [single] and [singleOrNull].
 * Returns single element, or null if the collection is empty.
 * If the collection has more than one element, throws [IllegalArgumentException].
 *
 * This is less surprising than [singleOrNull]
 * because it doesn't return null when there are multiple items in the collection.
 */
@Suppress("ReplaceSizeZeroCheckWithIsEmpty")
public fun <T> Sequence<T>.singleNullOrThrow(): T? {
  val iterator = iterator()
  if (!iterator.hasNext()) return null
  val single = iterator.next()
  if (!iterator.hasNext()) return single
  throw IllegalArgumentException("Collection has more than one element.")
}

/**
 * Adapted from [single] and [singleOrNull].
 * Returns the single element matching the given [predicate], or null if element was not found.
 * If more than one element was found, throws [IllegalArgumentException].
 *
 * This is less surprising than [singleOrNull]
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
