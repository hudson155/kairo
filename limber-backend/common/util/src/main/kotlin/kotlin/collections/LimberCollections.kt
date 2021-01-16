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

/**
 * Adapted from [firstOrNull].
 */
fun <T> Iterable<T>.isNotEmpty(): Boolean {
  return when (this) {
    is List -> isNotEmpty()
    else -> iterator().hasNext()
  }
}

fun <T : Any> T?.ifNull(function: () -> Nothing): T = this ?: function()
