package limber.feature.sql

import java.util.Optional

public typealias Updater<M, U> = (M) -> U

public fun <T : Any> update(existing: T, new: T?): T {
  if (new == null) return existing
  return new
}

public fun <T : Any> update(existing: T?, new: Optional<T>?): T? {
  if (new == null) return existing
  return new.orElse(null)
}
