package kairo.rest.auth

import kotlin.time.Duration
import kotlinx.serialization.Serializable

@Serializable
public data class VerifierConfig(
  val jwkUrl: String,
  val issuer: String,
  val audience: String? = null,
  val claims: Map<String, String> = emptyMap(),
  val leeway: Duration,
)
