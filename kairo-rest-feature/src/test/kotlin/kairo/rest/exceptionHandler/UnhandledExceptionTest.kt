package kairo.rest.exceptionHandler

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UnhandledExceptionTest : ExceptionHandlerTest() {
  @Test
  fun test(): Unit = runTest {
    mock { throw IllegalStateException("Something went wrong.") }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.InternalServerError)
    response.shouldBeNull()
  }
}
