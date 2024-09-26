package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class EndpointNotFoundTest : ExceptionHandlerTest() {
  @Test
  fun test(): Unit = runTest {
    val (statusCode, response) = request {
      url {
        path("/something-else")
      }
    }

    statusCode.shouldBe(HttpStatusCode.NotFound)
    response.shouldBe(
      mapOf(
        "type" to "EndpointNotFound",
        "message" to "This endpoint does not exist. Check the method, path, accept header, content type header, and query params.",
      ),
    )
  }
}
