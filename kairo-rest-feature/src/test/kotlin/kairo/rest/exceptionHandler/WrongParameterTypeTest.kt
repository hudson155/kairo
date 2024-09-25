package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class WrongParameterTypeTest : ExceptionHandlerTest() {
  @Test
  fun `number in place of string`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "firstName": "Patrick", "lastName": 42 },
              { "firstName": "Betsy", "lastName": "Wollheim" }
            ]
          }
        """.trimIndent(),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "location": {
            "column": 43,
            "line": 3
          },
          "message": "Wrong parameter type.",
          "path": "authors[0].lastName",
          "type": "WrongParameterType"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `number in place of object`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              42,
              { "firstName": "Betsy", "lastName": "Wollheim" }
            ]
          }
        """.trimIndent(),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "location": {
            "column": 5,
            "line": 3
          },
          "message": "Wrong parameter type.",
          "path": "authors[0]",
          "type": "WrongParameterType"
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
            "authors": 42
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
          "message": "Wrong parameter type.",
          "path": "authors",
          "type": "WrongParameterType"
        }
      """.trimIndent(),
    )
  }
}
