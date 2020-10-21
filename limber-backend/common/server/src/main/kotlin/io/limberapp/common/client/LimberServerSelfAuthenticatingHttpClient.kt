package io.limberapp.common.client

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import io.ktor.http.HttpHeaders
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.auth.jwt.withJwt
import io.limberapp.common.permissions.AccountRole
import java.time.Duration
import java.time.ZonedDateTime
import java.util.*

private val jwtLifespan = Duration.ofHours(1)
private val cacheDuration = jwtLifespan.minusMinutes(1)

/**
 * Generates JWTs used in server-to-server communication.
 */
internal class LimberServerSelfAuthenticatingHttpClient(
    baseUrl: String,
    private val algorithm: Algorithm,
    private val issuer: String?,
) : LimberHttpClientImpl(baseUrl) {
  private val jwtCache: LoadingCache<Unit, String> = CacheBuilder.newBuilder()
      .expireAfterWrite(cacheDuration)
      .build(CacheLoader.from { -> createJwtString() })

  override fun LimberHttpClientRequestBuilder.rootBuilder() {
    header(HttpHeaders.Authorization, "Bearer ${getJwtString()}")
  }

  private fun getJwtString() = jwtCache.get(Unit)

  private fun createJwtString(): String {
    val jwt = Jwt.withOnlyRole(AccountRole.LIMBER_SERVER)
    val now = ZonedDateTime.now()
    return JWT.create()
        .withJwt(jwt)
        .withIssuer(issuer)
        .withIssuedAt(Date.from(now.toInstant()))
        .withExpiresAt(Date.from(now.plus(jwtLifespan).toInstant()))
        .sign(algorithm)
  }
}
