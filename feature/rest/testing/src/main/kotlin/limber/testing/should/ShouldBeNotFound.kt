package limber.testing.should

import io.kotest.matchers.nulls.shouldBeNull

public suspend fun shouldNotBeFound(block: suspend () -> Any?) {
  block().shouldBeNull()
}
