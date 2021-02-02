package io.limberapp.testing.integration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.limberapp.common.auth.jwt.JwtClaims
import io.limberapp.common.client.HttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder
import io.limberapp.common.client.RequestBuilder
import io.limberapp.common.client.SUCCESSFUL_RESPONSE_RANGE
import io.limberapp.common.client.exception.LimberHttpClientException
import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import io.limberapp.common.restInterface.Endpoint
import io.limberapp.common.serialization.LimberObjectMapper

/**
 * Instead of sending requests to an actual server, interfaces directly with a Ktor
 * [TestApplicationEngine] to avoid HTTP round trips.
 */
class IntegrationTestHttpClient(
    private val engine: TestApplicationEngine,
) : HttpClient(LimberObjectMapper()) {
  internal val jwt: String = run {
    val permissions = LimberPermissions(setOf(LimberPermission.SUPERUSER))
    return@run JWT.create()
        .withClaim(JwtClaims.permissions, permissions.asDarb())
        .sign(Algorithm.none())
  }

  override suspend fun request(
      endpoint: Endpoint,
      builder: RequestBuilder?,
  ): Pair<HttpStatusCode, String> {
    val call = engine.handleRequest(endpoint.httpMethod, endpoint.href) {
      val requestBuilder = LimberHttpClientRequestBuilder(accept = endpoint.contentType).apply {
        putHeader(HttpHeaders.Authorization, HttpAuthHeader.Single("Bearer", jwt))
        builder?.let { it() }
      }
      requestBuilder.headers.forEach { (key, value) -> addHeader(key, value) }
      endpoint.body?.let {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(objectMapper.writeValueAsString(it))
      }
      /**
       * Even though the Accept header was already set, we append application/json for parity with
       * the real implementation of [HttpClient], which sets 2 Accept headers.
       */
      addHeader(HttpHeaders.Accept, ContentType.Application.Json.toString())
    }
    val statusCode = checkNotNull(call.response.status())
    val responseBody = checkNotNull(call.response.content)
    if (statusCode.value !in SUCCESSFUL_RESPONSE_RANGE) {
      throw LimberHttpClientException(statusCode, responseBody)
    }
    return Pair(statusCode, responseBody)
  }

  override fun close(): Unit = Unit
}
