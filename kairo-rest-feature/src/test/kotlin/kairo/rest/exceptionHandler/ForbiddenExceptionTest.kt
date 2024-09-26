package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kairo.exception.ForbiddenException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ForbiddenExceptionTest : ExceptionHandlerTest() {
  internal class MyException : ForbiddenException()

  @Test
  fun test(): Unit = runTest {
    mock { throw MyException() }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.Forbidden)
    response.shouldBe(
      mapOf(
        "type" to "MyException",
      ),
    )
  }
}
