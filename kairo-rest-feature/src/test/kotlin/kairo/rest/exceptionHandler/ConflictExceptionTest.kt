package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kairo.exception.ConflictException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ConflictExceptionTest : ExceptionHandlerTest() {
  internal class MyException : ConflictException("Custom test message.")

  @Test
  fun test(): Unit = runTest {
    mock { throw MyException() }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.Conflict)
    response.shouldBe(
      mapOf(
        "type" to "MyException",
        "message" to "Custom test message.",
      ),
    )
  }
}
