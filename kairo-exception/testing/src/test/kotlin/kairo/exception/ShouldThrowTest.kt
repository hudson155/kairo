package kairo.exception

import io.kotest.assertions.shouldFail
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Test

internal class ShouldThrowTest {
  internal data class Example(
    val key: String,
  ) : LogicalFailure("Example") {
    override val type: String = "Example"
    override val status: HttpStatusCode = HttpStatusCode.InternalServerError

    override fun JsonObjectBuilder.buildJson() {
      put("key", JsonPrimitive(key))
    }
  }

  @Test
  fun `happy path`(): Unit =
    runTest {
      shouldNotThrowAny {
        shouldThrow(Example("expected")) {
          throw Example("expected")
        }
      }
    }

  @Test
  fun `exception type mismatch`(): Unit =
    runTest {
      shouldFail {
        shouldThrow(Example("expected")) {
          throw IllegalArgumentException("expected")
        }
      }
    }

  @Test
  fun `property content mismatch`(): Unit =
    runTest {
      shouldFail {
        shouldThrow(Example("expected")) {
          throw Example("actual")
        }
      }
    }
}
