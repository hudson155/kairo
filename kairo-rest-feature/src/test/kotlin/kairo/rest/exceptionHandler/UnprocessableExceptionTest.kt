package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kairo.exception.NotFoundException
import kairo.exception.unprocessable
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UnprocessableExceptionTest : ExceptionHandlerTest() {
  internal class MyException : NotFoundException("Custom test message.")

  @Test
  fun test(): Unit = runTest {
    mock { throw unprocessable(MyException()) }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.UnprocessableEntity)
    response.shouldBe(
      mapOf(
        "type" to "MyException",
        "message" to "Custom test message.",
      ),
    )
  }
}
