package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class MalformedContentTypeTest : ExceptionHandlerTest() {
  @Test
  fun test(): Unit = runTest {
    mock { throw BadContentTypeFormatException("Custom test message.") }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "MalformedContentType",
        "message" to "Malformed content type. The content type in one of the HTTP headers was not well-formed.",
      ),
    )
  }
}
