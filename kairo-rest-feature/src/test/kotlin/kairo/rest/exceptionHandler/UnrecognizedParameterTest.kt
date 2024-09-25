package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UnrecognizedParameterTest : ExceptionHandlerTest() {
  @Test
  fun test(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "firstName": "Patrick", "middleName": "James", "lastName": "Rothfuss" },
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
            "column": 78,
            "line": 3
          },
          "message": "Unrecognized parameter.",
          "path": "authors[0].middleName",
          "type": "UnrecognizedParameter"
        }
      """.trimIndent(),
    )
  }
}
