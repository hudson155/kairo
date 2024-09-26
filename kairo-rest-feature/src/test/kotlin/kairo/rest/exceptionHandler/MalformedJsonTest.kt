package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class MalformedJsonTest : ExceptionHandlerTest() {
  @Test
  fun `unquoted string`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
              { "type": "Named", "firstName": "Betsy", "lastName": "Wollheim" }
            ],
            "type": Print
          }
        """.trimIndent(),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "message": "Malformed JSON. The JSON provided was not well-formed.",
          "type": "MalformedJson"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `missing brace`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
              { "type": "Named", "firstName": "Betsy", "lastName": "Wollheim" }
            ],
            "type": "Print"
        """.trimIndent(),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "message": "Malformed JSON. The JSON provided was not well-formed.",
          "type": "MalformedJson"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `extra brace`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
              { "type": "Named", "firstName": "Betsy", "lastName": "Wollheim" }}
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
          "message": "Malformed JSON. The JSON provided was not well-formed.",
          "type": "MalformedJson"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `missing quote`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
              { "type": "Named", "firstName": "Betsy", "lastName": "Wollheim" }
            ],
            "type": "Print
          }
        """.trimIndent(),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "message": "Malformed JSON. The JSON provided was not well-formed.",
          "type": "MalformedJson"
        }
      """.trimIndent(),
    )
  }

  @Test
  fun `extra quote`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
              { "type": "Named", "firstName": "Betsy", "lastName": "Wollheim" }
            ],
            "type": "Print""
          }
        """.trimIndent(),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "message": "Malformed JSON. The JSON provided was not well-formed.",
          "type": "MalformedJson"
        }
      """.trimIndent(),
    )
  }
}
