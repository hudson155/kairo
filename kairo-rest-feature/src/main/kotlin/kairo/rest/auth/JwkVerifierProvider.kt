package kairo.rest.auth

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.net.URI
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.ConcurrentHashMap

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
public class JwkVerifierProvider internal constructor(
  jwksEndpoint: String,
  private val leewaySec: Long,
) {
  private val provider: JwkProvider = JwkProviderBuilder(URI(jwksEndpoint).normalize().toURL()).build()

  private val verifiers: MutableMap<String, JWTVerifier> = ConcurrentHashMap()

  /**
   * Attempts to get an existing [JWTVerifier] from the [verifiers] cache,
   * using [provider] if necessary.
   *
   * [provider] has a built-in cache of its own.
   */
  public operator fun get(keyId: String): JWTVerifier? =
    verifiers.compute(keyId) { _, verifier ->
      if (verifier != null) return@compute verifier
      val jwk = provider[keyId] ?: return@compute null
      val algorithm = createAlgorithm(jwk)
      return@compute JWT.require(algorithm).acceptLeeway(leewaySec).build() // Claim verification uses a real clock.
    }

  @Suppress("UseIfInsteadOfWhen")
  private fun createAlgorithm(jwk: Jwk): Algorithm =
    when (val algorithm = jwk.algorithm) {
      "RS256" -> Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
      else -> throw IllegalArgumentException("Unsupported algorithm $algorithm.") // Add more as necessary.
    }
}
