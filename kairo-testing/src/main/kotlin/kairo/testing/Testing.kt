package kairo.testing

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
public inline fun <T, R> T.setup(
  @Suppress("UNUSED_PARAMETER")
  description: String? = null, // For readability on the caller's side.
  block: T.() -> R,
): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return block()
}

@OptIn(ExperimentalContracts::class)
public inline fun <T, R> T.precondition(
  @Suppress("UNUSED_PARAMETER")
  description: String? = null, // For readability on the caller's side.
  block: T.() -> R,
): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return block()
}

@OptIn(ExperimentalContracts::class)
public inline fun <T, R> T.test(
  @Suppress("UNUSED_PARAMETER")
  description: String? = null, // For readability on the caller's side.
  block: T.() -> R,
): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return block()
}

@OptIn(ExperimentalContracts::class)
public inline fun <T, R> T.postcondition(
  @Suppress("UNUSED_PARAMETER")
  description: String? = null, // For readability on the caller's side.
  block: T.() -> R,
): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return block()
}
