package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kairo.exception.TooManyRequestsException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class TooManyRequestsExceptionTest : ExceptionHandlerTest() {
  internal class MyException : TooManyRequestsException("Custom test message.")

  @Test
  fun test(): Unit = runTest {
    mock { throw MyException() }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.TooManyRequests)
    response.shouldBe(
      mapOf(
        "type" to "MyException",
        "message" to "Custom test message.",
      ),
    )
  }
}
