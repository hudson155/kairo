package kotlin.collections

/**
 * Adapted from [single] and [singleOrNull].
 */
fun <T> Iterable<T>.singleNullOrThrow(): T? {
  when (this) {
    is List -> when (size) {
      0 -> return null
      1 -> return this[0]
    }
    else -> {
      val iterator = iterator()
      if (!iterator.hasNext()) return null
      val single = iterator.next()
      if (!iterator.hasNext()) return single
    }
  }
  throw IllegalArgumentException("Collection has more than one element.")
}

fun Any?.ifNull(function: () -> Nothing) = this ?: function()
