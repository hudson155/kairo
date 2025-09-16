package kairo.optional

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RequiredGetOrThrowTest {
  @Test
  fun missing(): Unit =
    runTest {
      shouldThrow<IllegalStateException> {
        Required.Missing.getOrThrow()
      }
    }

  @Test
  fun value(): Unit =
    runTest {
      Required.Value(42).getOrThrow()
        .shouldBe(42)
    }
}
