package kairo.optional

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class OptionalGetOrThrowTest {
  @Test
  fun missing(): Unit =
    runTest {
      shouldThrow<IllegalStateException> {
        Optional.Missing.getOrThrow()
      }
    }

  @Test
  fun `null`(): Unit =
    runTest {
      Optional.Null.getOrThrow()
        .shouldBeNull()
    }

  @Test
  fun value(): Unit =
    runTest {
      Optional.Value(42).getOrThrow()
        .shouldBe(42)
    }
}
