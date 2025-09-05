package kairo.util

/**
 * Returns the first recursive cause of the specified type, or null if there is no matching cause.
 */
public inline fun <reified T> Throwable.firstCauseOf(): T? =
  generateSequence(this) { it.cause }
    .filterIsInstance<T>()
    .firstOrNull()
