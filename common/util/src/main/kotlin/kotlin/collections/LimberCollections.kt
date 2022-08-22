@file:Suppress("InvalidPackageDeclaration")

package kotlin.collections

/**
 * Adapted from [single] and [singleOrNull].
 * Returns single element, or null if the collection is empty.
 * If the collection has more than one element, throws [IllegalArgumentException].
 */
public fun <T> Iterable<T>.singleNullOrThrow(): T? {
  if (this is List) {
    if (size == 0) return null
    if (size == 1) return this[0]
  } else {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    val single = iterator.next()
    if (!iterator.hasNext()) return single
  }
  throw IllegalArgumentException("Collection has more than one element.")
}
