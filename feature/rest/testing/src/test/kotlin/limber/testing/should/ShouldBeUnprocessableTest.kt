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
import limber.rep.UnprocessableErrorRep
import org.junit.jupiter.api.Test

internal class ShouldBeUnprocessableTest {
  @Test
  fun `no exception`() {
    runBlocking {
      val e = shouldThrow<AssertionError> {
        shouldBeUnprocessable("expected message") {}
      }
      e.shouldHaveMessage(
        "Expected exception io.ktor.client.plugins.ClientRequestException" +
          " but no exception was thrown.",
      )
    }
  }

  @Test
  fun `incorrect exception type`() {
    runBlocking {
      val e = shouldThrow<AssertionError> {
        shouldBeUnprocessable("expected message") {
          val response = createHttpResponse()
          throw ServerResponseException(response, "")
        }
      }
      e.shouldHaveMessage(
        "Expected exception io.ktor.client.plugins.ClientRequestException" +
          " but a ServerResponseException was thrown instead.",
      )
    }
  }

  @Test
  fun `incorrect exception status`() {
    runBlocking {
      val e = shouldThrow<AssertionError> {
        shouldBeUnprocessable("expected message") {
          val response = createHttpResponse()
          throw ClientRequestException(response, "")
        }
      }
      e.shouldHaveMessage("expected:<422 Unprocessable Entity> but was:<200 OK>")
    }
  }

  @Test
  fun `no body`() {
    runBlocking {
      val e = shouldThrow<NullPointerException> {
        shouldBeUnprocessable("expected message") {
          val response = createHttpResponse(HttpStatusCode.UnprocessableEntity)
          throw ClientRequestException(response, "")
        }
      }
      e.shouldHaveMessage("null cannot be cast to non-null type limber.rep.UnprocessableErrorRep")
    }
  }

  @Test
  fun `incorrect body message`() {
    runBlocking {
      val e = shouldThrow<AssertionError> {
        shouldBeUnprocessable("expected message") {
          val repsonse = createHttpResponse(
            statusCode = HttpStatusCode.UnprocessableEntity,
            body = UnprocessableErrorRep("actual message"),
          )
          throw ClientRequestException(repsonse, "")
        }
      }
      e.shouldHaveMessage("expected:<\"expected message\"> but was:<\"actual message\">")
    }
  }

  @Test
  fun `happy path`() {
    runBlocking {
      shouldBeUnprocessable("expected message") {
        val repsonse = createHttpResponse(
          statusCode = HttpStatusCode.UnprocessableEntity,
          body = UnprocessableErrorRep("expected message"),
        )
        throw ClientRequestException(repsonse, "")
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
