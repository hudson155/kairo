package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class EntityNotFoundTest : ExceptionHandlerTest() {
  @Test
  fun test(): Unit = runTest {
    mock { null }
    val (statusCode, response) = request {}

    statusCode.shouldBe(HttpStatusCode.NotFound)
    response.shouldBe(
      mapOf(
        "type" to "EntityNotFound",
        "message" to "Entity not found.",
      ),
    )
  }
}
