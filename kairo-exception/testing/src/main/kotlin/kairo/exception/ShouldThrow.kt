package kairo.exception

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

public inline fun shouldThrow(properties: LogicalFailure.Properties, block: () -> Any?): LogicalFailure {
  val logicalFailure = shouldThrow<LogicalFailure>(block)
  logicalFailure.properties.shouldBe(properties)
  return logicalFailure
}
