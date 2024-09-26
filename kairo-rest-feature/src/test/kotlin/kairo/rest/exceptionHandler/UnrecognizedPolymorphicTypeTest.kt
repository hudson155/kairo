package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UnrecognizedPolymorphicTypeTest : ExceptionHandlerTest() {
  @Test
  fun test(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Ai", "firstName": "ChatGPT" }
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
            "column": 15,
            "line": 3
          },
          "message": "Unrecognized polymorphic type. This property could be one of several types, but the given type was not recognized.",
          "path": "authors[0]",
          "type": "UnrecognizedPolymorphicType"
        }
      """.trimIndent(),
    )
  }
}
