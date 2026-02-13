package kairo.rest.auth

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import io.ktor.server.auth.BearerTokenCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.RoutingCall
import java.net.URI
import java.security.interfaces.RSAPublicKey
import kairo.rest.RestEndpoint
import kairo.rest.exception.ExpiredJwt
import kairo.rest.exception.JwtVerificationFailed
import kairo.rest.exception.NoJwt
import kotlinx.coroutines.CancellationException

public class AuthReceiver<E : RestEndpoint<*, *>> internal constructor(
  public val call: RoutingCall,
  public val endpoint: E,
  private val default: suspend AuthReceiver<E>.() -> Unit,
) {
  private var result: Result<Unit>? = null

  internal suspend fun auth(auth: suspend AuthReceiver<E>.() -> Unit) {
    val result = this.result
    if (result != null && result.isFailure) return
    this.result = attempt(auth)
  }

  internal suspend fun authOverriddenBy(auth: suspend AuthReceiver<E>.() -> Unit) {
    val result = this.result
    if (result == null || result.isSuccess) return
    attempt(auth).let { if (it.isSuccess) this.result = it }
  }

  internal suspend fun getOrThrow() {
    (this.result ?: attempt(this.default)).getOrThrow()
  }

  private suspend fun attempt(auth: suspend AuthReceiver<E>.() -> Unit): Result<Unit> {
    try {
      return Result.success(auth())
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      return Result.failure(e)
    }
  }

  public companion object {
    private val sentinel: RestEndpoint<Unit, Unit> = object : RestEndpoint<Unit, Unit>() {}

    /**
     * Creates an [AuthReceiver] for call-level auth checks outside of REST endpoints
     * (e.g. admin dashboard routes). The [endpoint] property should not be accessed.
     */
    @Suppress("UNCHECKED_CAST")
    public fun forCall(call: RoutingCall): AuthReceiver<*> =
      AuthReceiver(
        call = call,
        endpoint = sentinel,
        default = { },
      )
  }
}

public fun AuthReceiver<*>.public(): Unit =
  Unit

public fun AuthReceiver<*>.verify(config: VerifierConfig): JWTPrincipal {
  val credential = call.principal<BearerTokenCredential>() ?: throw NoJwt()
  val verifier = createVerifier(config, credential)
  val payload = try {
    verifier.verify(credential.token)
  } catch (e: TokenExpiredException) {
    throw ExpiredJwt(e)
  } catch (e: JWTVerificationException) {
    throw JwtVerificationFailed(e)
  }
  return JWTPrincipal(payload)
}

private fun createVerifier(
  config: VerifierConfig,
  credential: BearerTokenCredential,
): JWTVerifier {
  val jwkProvider = JwkProviderBuilder(URI.create(config.jwkUrl).toURL()).build()
  val jwk = try {
    getJwk(jwkProvider, credential)
  } catch (e: Exception) {
    throw JwtVerificationFailed(e)
  }
  val algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
  return JWT.require(algorithm).apply {
    this.withIssuer(config.issuer)
    config.audience?.let { withAudience(it) }
    config.claims.forEach { (key, value) -> withClaim(key, value) }
    acceptLeeway(config.leeway.inWholeSeconds)
  }.build()
}

private fun getJwk(
  jwkProvider: JwkProvider,
  credential: BearerTokenCredential,
): Jwk {
  val decoded = JWT.decode(credential.token)
  return jwkProvider.get(decoded.keyId)
}
