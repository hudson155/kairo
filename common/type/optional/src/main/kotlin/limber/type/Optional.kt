package limber.type

import java.util.Optional

public fun <T : Any> Optional<T>?.or(existing: T?): T? =
  when {
    this == null -> existing
    isEmpty -> null
    else -> get()
  }
