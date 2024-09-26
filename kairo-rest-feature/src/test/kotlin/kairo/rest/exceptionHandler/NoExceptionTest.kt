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
      """
        {
          "authors": [
            {
              "type": "Named",
              "firstName": "Patrick",
              "lastName": "Rothfuss"
            },
            {
              "type": "Named",
              "firstName": "Betsy",
              "lastName": "Wollheim"
            }
          ],
          "title": "The Name of the Wind",
          "type": "Print"
        }
      """.trimIndent(),
    )
  }
}
