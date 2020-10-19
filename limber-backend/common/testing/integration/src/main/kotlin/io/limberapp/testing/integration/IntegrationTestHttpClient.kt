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
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.auth.jwt.withJwt
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.permissions.AccountRole

class IntegrationTestHttpClient(private val engine: TestApplicationEngine) : LimberHttpClient() {
  override suspend fun request(
      endpoint: LimberEndpoint,
      builder: LimberHttpClientRequestBuilder.() -> Unit,
  ): Pair<HttpStatusCode, String> {
    val call = engine.handleRequest(endpoint.httpMethod, endpoint.href) {
      val requestBuilder = LimberHttpClientRequestBuilder(accept = endpoint.contentType).apply {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        createAuthHeader()?.let { header(HttpHeaders.Authorization, it) }
        builder()
      }
      requestBuilder.headers.forEach { (key, value) -> addHeader(key, value) }
      endpoint.body?.let { setBody(objectMapper.writeValueAsString(it)) }
    }
    return Pair(checkNotNull(call.response.status()), checkNotNull(call.response.content))
  }

  private fun createAuthHeader(): HttpAuthHeader? {
    val jwt = Jwt.withOnlyRole(AccountRole.SUPERUSER)
    val jwtString = JWT.create().withJwt(jwt).sign(Algorithm.none())
    return HttpAuthHeader.Single("Bearer", jwtString)
  }

  override fun close() = Unit
}
