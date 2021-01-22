package io.limberapp.common.client

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.content.TextContent
import io.ktor.http.HttpStatusCode
import io.ktor.util.toMap
import io.limberapp.backend.api.typicodePost.TypicodePostApi
import io.limberapp.backend.rep.typicodePost.TypicodePostRep
import io.limberapp.common.client.exception.LimberHttpClientException
import io.limberapp.common.serialization.LimberObjectMapper
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private data class Request(
    val url: String,
    val method: String,
    val headers: Map<String, List<String>>,
    val contentType: String?,
    val body: String?,
) {
  companion object {
    fun fromKtorRequestData(request: HttpRequestData): Request = Request(
        url = request.url.toString(),
        method = request.method.value,
        headers = request.headers.toMap(),
        contentType = (request.body as? TextContent)?.contentType?.toString(),
        body = (request.body as? TextContent)?.text,
    )
  }
}

@Suppress("BlockingMethodInNonBlockingContext")
internal class MockHttpClientImplTest {
  private val objectMapper: LimberObjectMapper = LimberObjectMapper()

  /**
   * [engineFactory] is adapted from [MockEngine]. It returns a JSON-serialized version of the
   * request, allowing the client to inspect whether or not the request was received correctly.
   */
  private val engineFactory: HttpClientEngineFactory<MockEngineConfig> =
      object : HttpClientEngineFactory<MockEngineConfig> {
        override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine =
            MockEngine(MockEngineConfig().apply {
              addHandler { request ->
                val responseBody =
                    objectMapper.writeValueAsString(Request.fromKtorRequestData(request))
                val statusCode =
                    if (request.url.toString().endsWith("conflict")) HttpStatusCode.Conflict
                    else HttpStatusCode.OK
                return@addHandler respond(responseBody, statusCode)
              }
              block()
            })
      }

  private val httpClient: HttpClient =
      HttpClientImpl(engineFactory, "http://mock.tld", objectMapper)

  @Test
  fun get(): Unit = runBlocking {
    val result: Request = httpClient.request(TypicodePostApi.Get(postId = 2), null) {
      readValue(checkNotNull(it))
    }
    assertEquals(Request(
        url = "http://mock.tld/posts/2",
        method = "GET",
        headers = mapOf(
            "Accept" to listOf("application/json", "application/json"),
            "Accept-Charset" to listOf("UTF-8"),
        ),
        contentType = null,
        body = null,
    ), result)
  }

  @Test
  fun post(): Unit = runBlocking {
    val result: Request = httpClient.request(TypicodePostApi.Post(
        rep = TypicodePostRep.Creation(title = "foo", body = "bar", userId = 1)
    ), null) {
      readValue(checkNotNull(it))
    }
    assertEquals(Request(
        url = "http://mock.tld/posts",
        method = "POST",
        headers = mapOf(
            "Accept" to listOf("application/json", "application/json"),
            "Accept-Charset" to listOf("UTF-8"),
        ),
        contentType = "application/json",
        body = "{\"userId\":1,\"title\":\"foo\",\"body\":\"bar\"}",
    ), result)
  }

  @Test
  fun conflict(): Unit = runBlocking {
    assertFailsWith<LimberHttpClientException> {
      httpClient.request(TypicodePostApi.Conflict, null) {
        readValue(checkNotNull(it))
      }
    }.let {
      assertEquals(HttpStatusCode.Conflict, it.statusCode)
      assertEquals(Request(
          url = "http://mock.tld/conflict",
          method = "GET",
          headers = mapOf(
              "Accept" to listOf("application/json", "application/json"),
              "Accept-Charset" to listOf("UTF-8"),
          ),
          contentType = null,
          body = null,
      ), objectMapper.readValue(it.errorMessage))
    }
  }
}
