package kairo.closeable

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Similar to [java.io.Closeable].
 */
public interface KairoCloseable {
  public fun close()

  /**
   * Similar to [java.io.Closeable], but supports coroutines.
   */
  public interface Suspend {
    public suspend fun close()
  }
}

@OptIn(ExperimentalContracts::class)
public fun <T : KairoCloseable, R> T.use(block: (T) -> R): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return use(block = { block(this) }, close = { close() })
}

@OptIn(ExperimentalContracts::class)
public suspend fun <T : KairoCloseable.Suspend, R> T.use(block: (T) -> R): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return use(block = { block(this) }, close = { close() })
}

private inline fun <R> use(
  block: () -> R,
  close: () -> Unit,
): R {
  var exception: Throwable? = null
  try {
    return block()
  } catch (e: Throwable) {
    exception = e
    throw e
  } finally {
    try {
      close()
    } catch (closeException: Throwable) {
      exception?.addSuppressed(closeException)
    }
  }
}
