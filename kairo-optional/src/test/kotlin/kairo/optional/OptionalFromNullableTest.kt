package kairo.optional

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class OptionalFromNullableTest {
  @Test
  fun `null`(): Unit =
    runTest {
      Optional.fromNullable(null)
        .shouldBe(Optional.Null)
    }

  @Test
  fun `non-null`(): Unit =
    runTest {
      Optional.fromNullable(42)
        .shouldBe(Optional.Value(42))
    }
}
