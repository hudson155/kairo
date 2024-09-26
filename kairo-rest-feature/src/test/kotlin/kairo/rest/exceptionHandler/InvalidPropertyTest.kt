package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class InvalidPropertyTest : ExceptionHandlerTest() {
  @Test
  fun `number in place of string`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        mapOf(
          "authors" to listOf(
            mapOf("type" to "Named", "firstName" to "Patrick", "lastName" to 42),
            mapOf("type" to "Named", "firstName" to "Betsy", "lastName" to "Wollheim"),
          ),
          "type" to "Print",
        ),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "InvalidProperty",
        "message" to "Invalid property.",
        "path" to "authors[0].lastName",
        "location" to mapOf("column" to 15, "line" to 6),
      ),
    )
  }

  @Test
  fun `number in place of array`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(mapOf(
        "authors" to 42,
        "type" to "Print",
      ),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "InvalidProperty",
        "message" to "Invalid property.",
        "path" to "authors",
        "location" to mapOf("line" to 2, "column" to 14),
      ),
    )
  }

  @Test
  fun `unsupported enum`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(mapOf(
        "authors" to listOf(
          mapOf("type" to "Named", "firstName" to "Patrick", "lastName" to "Rothfuss"),
          mapOf("type" to "Named", "firstName" to "Betsy", "lastName" to "Wollheim"),
        ),
        "type" to "Digital",
      ),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "InvalidProperty",
        "message" to "Invalid property.",
        "path" to "type",
        "location" to mapOf("line" to 14, "column" to 11),
      ),
    )
  }
}
