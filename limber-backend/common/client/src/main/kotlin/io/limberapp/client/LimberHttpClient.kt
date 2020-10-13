package io.limberapp.client

import com.fasterxml.jackson.core.JsonProcessingException
import io.ktor.http.HttpStatusCode
import io.limberapp.client.exception.LimberHttpClientException
import io.limberapp.client.exception.LimberHttpClientInternalException
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.serialization.Json
import io.limberapp.error.LimberError
import java.io.Closeable

private val SUCCESSFUL_RESPONSE_RANGE = 200..299

abstract class LimberHttpClient : Closeable {
  private val json = Json(prettyPrint = true)

  suspend fun <T> request(
      endpoint: LimberEndpoint,
      builder: LimberHttpClientRequestBuilder.() -> Unit,
      parseResponse: Json.(responseBody: String?) -> T,
  ): T {
    val (statusCode, responseBody) = request(endpoint, builder)
    return when (statusCode.value) {
      in SUCCESSFUL_RESPONSE_RANGE -> json.parseResponse(responseBody)
      HttpStatusCode.NotFound.value -> json.parseResponse(null)
      else -> {
        val limberError = try {
          json.parse<LimberError>(responseBody)
        } catch (e: JsonProcessingException) {
          throw LimberHttpClientInternalException(e)
        }
        throw LimberHttpClientException(limberError)
      }
    }
  }

  protected abstract suspend fun request(
      endpoint: LimberEndpoint,
      builder: LimberHttpClientRequestBuilder.() -> Unit,
  ): Pair<HttpStatusCode, String>
}
