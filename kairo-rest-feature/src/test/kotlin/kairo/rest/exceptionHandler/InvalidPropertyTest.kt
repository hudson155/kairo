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
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": 42 },
              { "type": "Named", "firstName": "Betsy", "lastName": "Wollheim" }
            ],
            "type": "Print"
          }
        """.trimIndent(),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "location": {
            "column": 60,
            "line": 3
          },
          "message": "Invalid property.",
          "path": "authors[0].lastName",
          "type": "InvalidProperty"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `number in place of array`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": 42,
            "type": "Print"
          }
        """.trimIndent(),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "location": {
            "column": 14,
            "line": 2
          },
          "message": "Invalid property.",
          "path": "authors",
          "type": "InvalidProperty"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `unsupported enum`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
              { "type": "Named", "firstName": "Betsy", "lastName": "Wollheim" }
            ],
            "type": "Digital"
          }
        """.trimIndent(),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "location": {
            "column": 11,
            "line": 6
          },
          "message": "Invalid property.",
          "path": "type",
          "type": "InvalidProperty"
        }
      """.trimIndent(),
    )
  }
}
