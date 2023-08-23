package limber.util.updater

import java.util.Optional

/**
 * An [Updater] is a function that transforms a rep.
 */
public typealias Updater<T> = (existing: T) -> T

/**
 * This utility wraps an existing [Updater], performing the [block] action after running the [Updater].
 * The [update] passed to [block] is the transformed rep (after the [Updater] has run).
 */
public operator fun <T : Any> Updater<T>.invoke(block: (update: T) -> Unit): Updater<T> =
  { existing ->
    this(existing).also(block)
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
 * If [new] is null, [existing], [existing] will be used.
 * If [new] is empty, sets the property to null.
 */
public fun <T : Any> update(existing: T?, new: Optional<T>?): T? {
  if (new == null) return existing
  return new.orElse(null)
}
