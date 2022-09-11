package limber.testing

import kotlinx.coroutines.runBlocking

public fun integrationTest(block: suspend () -> Unit) {
  runBlocking {
    block()
  }
}
