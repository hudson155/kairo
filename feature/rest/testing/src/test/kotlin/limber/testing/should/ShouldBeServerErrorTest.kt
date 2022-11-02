package limber.testing.should

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class ShouldBeServerErrorTest {
  @Test
  fun `no exception`() {
    runBlocking {
      val e = shouldThrow<AssertionError> {
        shouldBeServerError("expected message") {}
      }
      e.shouldHaveMessage(
        "Expected exception io.ktor.client.plugins.ServerResponseException" +
          " but no exception was thrown.",
      )
    }
  }

  @Test
  fun `incorrect exception type`() {
    runBlocking {
      val e = shouldThrow<AssertionError> {
        shouldBeServerError("expected message") {
          val response = createHttpResponse()
          throw ClientRequestException(response, "")
        }
      }
      e.shouldHaveMessage(
        "Expected exception io.ktor.client.plugins.ServerResponseException" +
          " but a ClientRequestException was thrown instead.",
      )
    }
  }

  @Test
  fun `incorrect exception status`() {
    runBlocking {
      val e = shouldThrow<AssertionError> {
        shouldBeServerError("expected message") {
          val response = createHttpResponse()
          throw ServerResponseException(response, "")
        }
      }
      e.shouldHaveMessage("expected:<500 Internal Server Error> but was:<200 OK>")
    }
  }

  @Test
  fun `no body`() {
    runBlocking {
      val e = shouldThrow<NullPointerException> {
        shouldBeServerError("expected message") {
          val response = createHttpResponse(HttpStatusCode.InternalServerError)
          throw ServerResponseException(response, "")
        }
      }
      e.shouldHaveMessage("null cannot be cast to non-null type kotlin.String")
    }
  }

  @Test
  fun `incorrect body message`() {
    runBlocking {
      val e = shouldThrow<AssertionError> {
        shouldBeServerError("expected message") {
          val response = createHttpResponse(
            statusCode = HttpStatusCode.InternalServerError,
            body = "actual message",
          )
          throw ServerResponseException(response, "")
        }
      }
      e.shouldHaveMessage("expected:<\"expected message\"> but was:<\"actual message\">")
    }
  }

  @Test
  fun `happy path`() {
    runBlocking {
      shouldBeServerError("expected message") {
        val response = createHttpResponse(
          statusCode = HttpStatusCode.InternalServerError,
          body = "expected message",
        )
        throw ServerResponseException(response, "")
      }
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
