package limber.testing

import kotlinx.coroutines.runBlocking

@Suppress("UNUSED_PARAMETER") // The description is for developer clarity.
public fun <T> testSetup(description: String, block: suspend () -> T): T =
  runBlocking {
    block()
  }

public fun test(block: suspend () -> Unit): Unit =
  runBlocking {
    block()
  }
