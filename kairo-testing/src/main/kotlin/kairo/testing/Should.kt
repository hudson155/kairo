package kairo.testing

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.shouldBe

public inline fun shouldThrow(e: Throwable, block: () -> Unit) {
  shouldThrowAny(block).shouldBe(e)
}
