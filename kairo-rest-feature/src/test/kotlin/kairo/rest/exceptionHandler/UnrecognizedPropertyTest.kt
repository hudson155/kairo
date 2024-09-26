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
        mapOf(
          "authors" to listOf(
            mapOf("type" to "Named", "firstName" to "Patrick", "middleName" to "James", "lastName" to "Rothfuss"),
            mapOf("type" to "Named", "firstName" to "Betsy", "lastName" to "Wollheim"),
          ),
          "type" to "Print",
        ),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "UnrecognizedProperty",
        "message" to "Unrecognized property. This property is not recognized. Is it named incorrectly?",
        "path" to "authors[0].middleName",
        "location" to mapOf("column" to 22, "line" to 7),
      ),
    )
  }

  @Test
  fun ignored(): Unit = runTest {
    val (statusCode, response) = request {
      setBody(
        mapOf(
          "authors" to listOf(
            mapOf("type" to "Named", "firstName" to "Patrick", "lastName" to "Rothfuss"),
            mapOf("type" to "Named", "firstName" to "Betsy", "lastName" to "Wollheim"),
          ),
          "firstAuthor" to mapOf("type" to "Named", "firstName" to "Patrick", "lastName" to "Rothfuss"),
          "type" to "Print",
        ),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "UnrecognizedProperty",
        "message" to "Unrecognized property. This property is not recognized. Is it named incorrectly?",
        "path" to "firstAuthor",
        "location" to mapOf("column" to 19, "line" to 14),
      ),
    )
  }
}
