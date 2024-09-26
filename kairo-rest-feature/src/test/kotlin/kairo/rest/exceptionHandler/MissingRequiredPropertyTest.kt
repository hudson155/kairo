package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class MissingRequiredPropertyTest : ExceptionHandlerTest() {
  @Test
  fun missing(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
              { "type": "Named", "lastName": "Wollheim" }
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
            "column": 47,
            "line": 4
          },
          "message": "Missing required property. This property is required, but was not provided or was null.",
          "path": "authors[1].firstName",
          "type": "MissingRequiredProperty"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `null`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
              { "type": "Named", "firstName": null, "lastName": "Wollheim" }
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
            "column": 66,
            "line": 4
          },
          "message": "Missing required property. This property is required, but was not provided or was null.",
          "path": "authors[1].firstName",
          "type": "MissingRequiredProperty"
        }
      """.trimIndent(),
    )
  }
}
