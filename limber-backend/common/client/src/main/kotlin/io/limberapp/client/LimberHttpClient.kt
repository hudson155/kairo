package io.limberapp.client

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpStatusCode
import io.limberapp.client.exception.LimberHttpClientException
import io.limberapp.client.exception.LimberHttpClientInternalException
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.serialization.limberObjectMapper
import io.limberapp.error.LimberError
import java.io.Closeable

private val SUCCESSFUL_RESPONSE_RANGE = 200..299

abstract class LimberHttpClient : Closeable {
  protected val objectMapper = limberObjectMapper(prettyPrint = true)

  suspend fun <T> request(
      endpoint: LimberEndpoint,
      builder: LimberHttpClientRequestBuilder.() -> Unit,
      parseResponse: ObjectMapper.(responseBody: String?) -> T,
  ): T {
    val (statusCode, responseBody) = request(endpoint, builder)
    return when (statusCode.value) {
      in SUCCESSFUL_RESPONSE_RANGE -> objectMapper.parseResponse(responseBody)
      HttpStatusCode.NotFound.value -> objectMapper.parseResponse(null)
      else -> {
        val limberError = try {
          objectMapper.readValue<LimberError>(responseBody)
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
