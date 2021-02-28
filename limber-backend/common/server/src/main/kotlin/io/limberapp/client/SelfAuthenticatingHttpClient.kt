package io.limberapp.client

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import io.ktor.http.HttpHeaders
import io.limberapp.auth.jwt.JwtClaims
import io.limberapp.permissions.limber.LimberPermission
import io.limberapp.permissions.limber.LimberPermissions
import io.limberapp.serialization.LimberObjectMapper
import java.time.Duration
import java.time.ZonedDateTime
import java.util.Date

private val jwtLifespan: Duration = Duration.ofHours(1)
private val cacheDuration: Duration = jwtLifespan.minusMinutes(1)

/**
 * Generates JWTs used in server-to-server communication. Uses in-memory caching to avoid generating
 * a new JWT for each request.
 */
class SelfAuthenticatingHttpClient(
    baseUrl: String,
    objectMapper: LimberObjectMapper,
    private val algorithm: Algorithm,
    private val issuer: String?,
) : HttpClientImpl(baseUrl, objectMapper) {
  private val jwtCache: LoadingCache<Unit, String> = CacheBuilder.newBuilder()
      .expireAfterWrite(cacheDuration)
      .build(CacheLoader.from(::createJwtString))

  override val rootBuilder: RequestBuilder = {
    putHeader(HttpHeaders.Authorization, "Bearer ${getJwtString()}")
  }

  private fun getJwtString() = jwtCache.get(Unit)

  private fun createJwtString(): String {
    val now = ZonedDateTime.now()
    val permissions = LimberPermissions(setOf(LimberPermission.SUPERUSER))
    return JWT.create()
        .withClaim(JwtClaims.permissions, permissions.asDarb())
        .withIssuer(issuer)
        .withIssuedAt(Date.from(now.toInstant()))
        .withExpiresAt(Date.from(now.plus(jwtLifespan).toInstant()))
        .sign(algorithm)
  }
}
