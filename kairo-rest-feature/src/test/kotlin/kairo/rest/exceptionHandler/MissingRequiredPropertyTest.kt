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
        mapOf(
          "authors" to listOf(
            mapOf("type" to "Named", "firstName" to "Patrick", "lastName" to "Rothfuss"),
            mapOf("type" to "Named", "lastName" to "Wollheim"),
          ),
          "type" to "Print",
        ),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "MissingRequiredProperty",
        "message" to "Missing required property. This property is required, but was not provided or was null.",
        "path" to "authors[1].firstName",
        "location" to mapOf("column" to 5, "line" to 11),
      ),
    )
  }

  @Test
  fun `null`(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        mapOf(
          "authors" to listOf(
            mapOf("type" to "Named", "firstName" to "Patrick", "lastName" to "Rothfuss"),
            mapOf("type" to "Named", "firstName" to null, "lastName" to "Wollheim"),
          ),
          "type" to "Print",
        ),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "MissingRequiredProperty",
        "message" to "Missing required property. This property is required, but was not provided or was null.",
        "path" to "authors[1].firstName",
        "location" to mapOf("column" to 5, "line" to 12),
      ),
    )
  }
}
