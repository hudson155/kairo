package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UnrecognizedPropertyTest : ExceptionHandlerTest() {
  @Test
  fun typical(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "middleName": "James", "lastName": "Rothfuss" },
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
            "column": 95,
            "line": 3
          },
          "message": "Unrecognized property. This property is not recognized. Is it named incorrectly?",
          "path": "authors[0].middleName",
          "type": "UnrecognizedProperty"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun ignored(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
              { "type": "Named", "firstName": "Betsy", "lastName": "Wollheim" }
            ],
            "firstAuthor": { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
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
            "column": 19,
            "line": 6
          },
          "message": "Unrecognized property. This property is not recognized. Is it named incorrectly?",
          "path": "firstAuthor",
          "type": "UnrecognizedProperty"
        }
      """.trimIndent(),
    )
  }
}
