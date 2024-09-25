package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class MissingRequiredParameterTest : ExceptionHandlerTest() {
  @Test
  fun test(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "title": "The Name of the Wind",
            "authors": [
              { "firstName": "Patrick", "lastName": "Rothfuss" },
              { "lastName": "Wollheim" }
            ]
          }
        """.trimIndent(),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "message": "Missing required parameter.",
          "path": "authors[1].firstName",
          "type": "MissingRequiredParameter"
        }
      """.trimIndent(),
    )
  }
}
