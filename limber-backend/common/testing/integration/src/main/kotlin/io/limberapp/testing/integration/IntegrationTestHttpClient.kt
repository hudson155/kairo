package io.limberapp.testing.integration

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.limberapp.auth.jwt.JwtClaims
import io.limberapp.auth.jwt.JwtUser
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.permissions.AccountRole
import java.util.*

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
    val jwt = JWT.create().withJwt(Jwt(
        org = null,
        roles = setOf(AccountRole.SUPERUSER),
        user = JwtUser(UUID.randomUUID(), null, null)
    )).sign(Algorithm.none())
    return HttpAuthHeader.Single("Bearer", jwt)
  }

  private fun JWTCreator.Builder.withJwt(jwt: Jwt): JWTCreator.Builder {
    withClaim(JwtClaims.org, jwt.org?.let { objectMapper.writeValueAsString(it) })
    withClaim(JwtClaims.roles, objectMapper.writeValueAsString(jwt.roles))
    withClaim(JwtClaims.user, jwt.user?.let { objectMapper.writeValueAsString(it) })
    return this
  }

  override fun close() = Unit
}
