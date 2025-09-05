package kairo.exception

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

public inline fun shouldThrow(expected: LogicalFailure, block: () -> Any?): LogicalFailure {
  val actual = shouldThrow<LogicalFailure>(block)
  actual.json.shouldBe(expected.json)
  return actual
}
