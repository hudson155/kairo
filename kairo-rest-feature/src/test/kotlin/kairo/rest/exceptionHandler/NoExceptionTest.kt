package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class NoExceptionTest : ExceptionHandlerTest() {
  @Test
  fun test(): Unit = runTest {
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.OK)
    response.shouldBe(
      mapOf(
        "title" to "The Name of the Wind",
        "authors" to listOf(
          mapOf("type" to "Named", "firstName" to "Patrick", "lastName" to "Rothfuss"),
          mapOf("type" to "Named", "firstName" to "Betsy", "lastName" to "Wollheim"),
        ),
        "isSeries" to true,
        "type" to "Print",
      ),
    )
  }
}
