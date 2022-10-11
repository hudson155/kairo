package limber.testing.should

import io.kotest.matchers.nulls.shouldBeNull

public inline fun shouldNotBeFound(block: () -> Any?) {
  block().shouldBeNull()
}
