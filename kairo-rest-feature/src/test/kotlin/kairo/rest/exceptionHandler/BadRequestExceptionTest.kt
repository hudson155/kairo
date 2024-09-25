package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kairo.exception.BadRequestException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BadRequestExceptionTest : ExceptionHandlerTest() {
  internal class MyException : BadRequestException("Custom test message.")

  @Test
  fun test(): Unit = runTest {
    mock { throw MyException() }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.BadRequest)
    response.shouldBe(
      """
        {
          "message": "Custom test message.",
          "type": "MyException"
        }
      """.trimIndent(),
    )
  }
}
