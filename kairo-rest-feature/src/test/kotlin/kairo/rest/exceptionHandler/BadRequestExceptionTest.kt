package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kairo.exception.BadRequestException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BadRequestExceptionTest : ExceptionHandlerTest() {
  internal class MyException : BadRequestException("Custom test message.")

  @Test
  fun `ktor exception`(): Unit = runTest {
    mock { throw io.ktor.server.plugins.BadRequestException("Custom test message.") }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "BadRequest",
      ),
    )
  }

  @Test
  fun `custom exception`(): Unit = runTest {
    mock { throw MyException() }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      mapOf(
        "type" to "MyException",
        "message" to "Custom test message.",
      ),
    )
  }
}
