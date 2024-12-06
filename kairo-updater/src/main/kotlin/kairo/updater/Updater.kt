package kairo.updater

import java.util.Optional

public fun interface Updater<T, U> {
  public suspend fun update(model: T): U
}

/**
 * Returns the [new] value, defaulting to the [existing] one.
 * Use this to update non-nullable properties.
 * Does not support setting the property to null.
 * If [new] is null, [existing] will be used.
 */
public fun <T : Any> update(existing: T, new: T?): T {
  if (new == null) return existing
  return new
}

/**
 * Returns the [new] value, defaulting to the [existing] one.
 * Use this to update nullable properties.
 * Supports setting the property to null.
 * If [new] is null, [existing] will be used.
 * If [new] is empty, sets the property to null.
 */
public fun <T : Any> update(existing: T?, new: Optional<T>?): T? {
  if (new == null) return existing
  return new.orElse(null)
}
