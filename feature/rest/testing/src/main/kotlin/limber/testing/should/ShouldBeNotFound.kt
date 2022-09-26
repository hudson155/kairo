package limber.testing.should

import io.kotest.matchers.shouldBe

public suspend fun shouldNotBeFound(block: suspend () -> Any?) {
  block().shouldBe(null)
}
