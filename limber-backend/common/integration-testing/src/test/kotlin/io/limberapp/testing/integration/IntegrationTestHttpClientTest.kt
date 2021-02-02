package io.limberapp.testing.integration

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.HttpStatusCode
import io.ktor.request.httpMethod
import io.ktor.request.path
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.server.testing.withTestApplication
import io.ktor.util.toMap
import io.limberapp.backend.api.post.PostApi
import io.limberapp.backend.rep.post.PostRep
import io.limberapp.common.client.HttpClient
import io.limberapp.common.client.exception.LimberHttpClientException
import io.limberapp.common.serialization.LimberObjectMapper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private data class Request(
    val path: String,
    val method: String,
    val headers: Map<String, List<String>>,
    val body: String?,
)

@Suppress("BlockingMethodInNonBlockingContext")
internal class MockHttpClientImplTest {
  private val objectMapper: LimberObjectMapper = LimberObjectMapper()

  @Test
  fun get(): Unit = test { httpClient ->
    val result: Request = httpClient.request(PostApi.Get(postId = 2), null) {
      readValue(checkNotNull(it))
    }
    assertEquals(Request(
        path = "/posts/2",
        method = "GET",
        headers = mapOf(
            "Authorization" to listOf("Bearer ${(httpClient as IntegrationTestHttpClient).jwt}"),
            "Accept" to listOf("application/json", "application/json"),
        ),
        body = null,
    ), result)
  }

  @Test
  fun post(): Unit = test { httpClient ->
    val result: Request = httpClient.request(PostApi.Post(
        rep = PostRep.Creation(title = "foo", body = "bar", userId = 1)
    ), null) {
      readValue(checkNotNull(it))
    }
    assertEquals(Request(
        path = "/posts",
        method = "POST",
        headers = mapOf(
            "Authorization" to listOf("Bearer ${(httpClient as IntegrationTestHttpClient).jwt}"),
            "Accept" to listOf("application/json", "application/json"),
            "Content-Type" to listOf("application/json"),
        ),
        body = "{\"userId\":1,\"title\":\"foo\",\"body\":\"bar\"}",
    ), result)
  }

  @Test
  fun conflict(): Unit = test { httpClient ->
    assertFailsWith<LimberHttpClientException> {
      httpClient.request(PostApi.Conflict, null) {
        readValue(checkNotNull(it))
      }
    }.let { e ->
      assertEquals(HttpStatusCode.Conflict, e.statusCode)
      assertEquals(Request(
          path = "/conflict",
          method = "GET",
          headers = mapOf(
              "Authorization" to listOf("Bearer ${(httpClient as IntegrationTestHttpClient).jwt}"),
              "Accept" to listOf("application/json", "application/json"),
          ),
          body = null,
      ), objectMapper.readValue(e.errorMessage))
    }
  }

  private fun test(block: suspend (HttpClient) -> Unit) {
    withTestApplication(
        moduleFunction = {
          install(Routing) {
            route("/{...}") {
              handle {
                val body = call.receiveOrNull<String>()?.ifEmpty { null }
                val request = Request(
                    path = call.request.path(),
                    method = call.request.httpMethod.value,
                    headers = call.request.headers.toMap(),
                    body = body,
                )
                val responseBody = objectMapper.writeValueAsString(request)
                val statusCode =
                    if (call.request.path().endsWith("conflict")) HttpStatusCode.Conflict
                    else HttpStatusCode.OK
                call.respond(statusCode, responseBody)
              }
            }
          }
        }, test = {
      val httpClient: HttpClient = IntegrationTestHttpClient(this)
      runBlocking { block(httpClient) }
    })
  }
}
