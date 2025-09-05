package kairo.exception

import io.kotest.assertions.shouldFail
import io.kotest.assertions.throwables.shouldNotThrowAny
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ShouldThrowTest {
  internal data class Properties(
    val key: String,
  ) : LogicalFailure.Properties()

  @Test
  fun `happy path`(): Unit =
    runTest {
      shouldNotThrowAny {
        shouldThrow(Properties("expected")) {
          logicalFailure(Properties("expected"))
        }
      }
    }

  @Test
  fun `exception type mismatch`(): Unit =
    runTest {
      shouldFail {
        shouldThrow(Properties("expected")) {
          throw IllegalArgumentException("expected")
        }
      }
    }

  @Test
  fun `property content mismatch`(): Unit =
    runTest {
      shouldFail {
        shouldThrow(Properties("expected")) {
          logicalFailure(Properties("actual"))
        }
      }
    }
}
