package io.limberapp.common.client

import io.ktor.http.HttpStatusCode
import io.limberapp.common.client.exception.LimberHttpClientException
import io.limberapp.common.restInterface.Endpoint
import io.limberapp.common.serialization.LimberObjectMapper
import java.io.Closeable

val SUCCESSFUL_RESPONSE_RANGE: IntRange = 200..299

abstract class HttpClient(protected val objectMapper: LimberObjectMapper) : Closeable {
  suspend fun <T> request(
      endpoint: Endpoint,
      builder: RequestBuilder?,
      parseResponse: LimberObjectMapper.(responseBody: String?) -> T,
  ): T {
    val (statusCode, responseBody) = try {
      request(endpoint, builder)
    } catch (e: LimberHttpClientException) {
      if (e.statusCode == HttpStatusCode.NotFound) {
        return objectMapper.parseResponse(null)
      }
      throw e
    }
    if (statusCode.value !in SUCCESSFUL_RESPONSE_RANGE) {
      error("Unexpected status code: $statusCode.")
    }
    return objectMapper.parseResponse(responseBody)
  }

  /**
   * Following a successful request, the implementation should return the HTTP response status code
   * and the response body as a string. If the request is unsuccessful (4xx or 5xx response code),
   * an instance of [LimberHttpClientException] should be thrown. Do not return a non-2xx response
   * code.
   */
  protected abstract suspend fun request(
      endpoint: Endpoint,
      builder: RequestBuilder?,
  ): Pair<HttpStatusCode, String>
}
