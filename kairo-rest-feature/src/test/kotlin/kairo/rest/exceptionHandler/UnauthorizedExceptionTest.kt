package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kairo.exception.UnauthorizedException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UnauthorizedExceptionTest : ExceptionHandlerTest() {
  internal class MyException : UnauthorizedException(null)

  @Test
  fun test(): Unit = runTest {
    mock { throw MyException() }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.Unauthorized)
    response.shouldBe(
      mapOf(
        "type" to "MyException",
      ),
    )
  }
}
