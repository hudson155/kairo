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

public class JwkVerifierProvider internal constructor(
  jwksEndpoint: String,
  private val leewaySec: Long,
) {
  private val provider: JwkProvider = JwkProviderBuilder(URI(jwksEndpoint).normalize().toURL()).build()

  private val verifiers: MutableMap<String, JWTVerifier> = ConcurrentHashMap()

  public operator fun get(keyId: String): JWTVerifier? {
    return verifiers.compute(keyId) { _, verifier ->
      if (verifier != null) return@compute verifier
      val jwk = provider[keyId] ?: return@compute null
      val algorithm = createAlgorithm(jwk)
      return@compute JWT.require(algorithm).acceptLeeway(leewaySec).build() // Claim verification uses a real clock.
    }
  }

  private fun createAlgorithm(jwk: Jwk): Algorithm {
    return when (val algorithm = jwk.algorithm) {
      "RS256" -> Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
      else -> error("Unsupported algorithm $algorithm.") // Add more as necessary.
    }
  }
}
