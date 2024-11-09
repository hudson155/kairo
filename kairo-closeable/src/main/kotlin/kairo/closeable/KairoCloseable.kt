package kairo.closeable

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public interface KairoCloseable {
  public fun close()

  public interface Suspend {
    public suspend fun close()
  }
}

@Suppress("DuplicatedCode")
@OptIn(ExperimentalContracts::class)
public inline fun <T : KairoCloseable, R> T.use(block: (T) -> R): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  var exception: Throwable? = null
  try {
    return block(this)
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

@Suppress("DuplicatedCode")
@OptIn(ExperimentalContracts::class)
public suspend inline fun <T : KairoCloseable.Suspend, R> T.use(block: (T) -> R): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  var exception: Throwable? = null
  try {
    return block(this)
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
