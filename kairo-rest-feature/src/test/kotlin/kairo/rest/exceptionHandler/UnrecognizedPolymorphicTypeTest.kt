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
        mapOf(
          "authors" to listOf(
            mapOf("type" to "Ai", "name" to "ChatGPT"),
          ),
          "type" to "Print",
        ),
      )
    }

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "UnrecognizedPolymorphicType",
        "message" to "Unrecognized polymorphic type." +
          " This property could be one of several types, but the given type was not recognized.",
        "path" to "authors[0]",
        "location" to mapOf("column" to 15, "line" to 5),
      ),
    )
  }
}
