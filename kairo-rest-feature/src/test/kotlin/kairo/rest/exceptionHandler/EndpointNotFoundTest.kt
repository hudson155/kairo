package kairo.rest.exceptionHandler

import io.kotest.matchers.shouldBe
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class EndpointNotFoundTest : ExceptionHandlerTest() {
  @Test
  fun `wrong method`(): Unit = runTest {
    val (statusCode, response) = request {
      method = HttpMethod.Put
    }

    statusCode.shouldBe(HttpStatusCode.NotFound)
    response.shouldBe(
      mapOf(
        "type" to "EndpointNotFound",
        "message" to "This endpoint does not exist." +
          " Check the method, path, and query params.",
      ),
    )
  }

  @Test
  fun `wrong path`(): Unit = runTest {
    val (statusCode, response) = request {
      url {
        path("/something-else")
      }
    }

    statusCode.shouldBe(HttpStatusCode.NotFound)
    response.shouldBe(
      mapOf(
        "type" to "EndpointNotFound",
        "message" to "This endpoint does not exist." +
          " Check the method, path, and query params.",
      ),
    )
  }

  @Test
  fun `missing query param`(): Unit = runTest {
    val (statusCode, response) = request {
      url {
        parameters.remove("title")
      }
    }

    statusCode.shouldBe(HttpStatusCode.NotFound)
    response.shouldBe(
      mapOf(
        "type" to "EndpointNotFound",
        "message" to "This endpoint does not exist." +
          " Check the method, path, and query params.",
      ),
    )
  }

  @Test
  fun `missing content type header`(): Unit = runTest {
    /*
     * I can't find a reasonable way to test this, because the Ktor client manages Content-Type headers internally.
     * Manual QA is required instead.
     *
     * Manually QA missing, malformed, and mismatched "Content-Type" headers.
     */
  }

  @Test
  fun `content type header mismatch`(): Unit = runTest {
    /*
     * I can't find a reasonable way to test this, because the Ktor client manages Content-Type headers internally.
     * Manual QA is required instead.
     *
     * Manually QA missing, malformed, and mismatched "Content-Type" headers.
     */
  }

  @Test
  fun `missing accept header`(): Unit = runTest {
    /*
     * I can't find a reasonable way to test this, because the Ktor client manages Accept headers internally.
     * Manual QA is required instead.
     *
     * Manually QA missing, malformed, and mismatched "Accept" headers.
     */
  }

  @Test
  fun `accept header mismatch`(): Unit = runTest {
    /*
     * I can't find a reasonable way to test this, because the Ktor client manages Accept headers internally.
     * Manual QA is required instead.
     *
     * Manually QA missing, malformed, and mismatched "Accept" headers.
     */
  }
}
