package limber.testing.should

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class ShouldNotBeFoundTest {
  @Test
  fun `exception thrown`() {
    runBlocking {
      val e = shouldThrow<ClientRequestException> {
        shouldNotBeFound {
          val response = createHttpResponse(HttpStatusCode.NotFound)
          throw ClientRequestException(response, "")
        }
      }
      e.shouldHaveMessage("Client request(GET http://jhudson.ca) invalid: 404 Not Found. Text: \"\"")
    }
  }

  @Test
  fun `incorrect return value`() {
    runBlocking {
      val e = shouldThrow<AssertionError> {
        shouldNotBeFound {}
      }
      e.shouldHaveMessage("Expected value to be null, but was kotlin.Unit.")
    }
  }

  @Test
  fun `happy path`() {
    runBlocking {
      shouldNotBeFound { null }
    }
  }

  private fun createHttpResponse(statusCode: HttpStatusCode = HttpStatusCode.OK, body: Any? = null): HttpResponse =
    mockk {
      every { call } returns mockk {
        every { request } returns mockk {
          every { method } returns HttpMethod.Get
          every { url } returns Url("http://jhudson.ca")
          every { status } returns statusCode
          coEvery { bodyNullable(any()) } returns body
        }
      }
    }
}
