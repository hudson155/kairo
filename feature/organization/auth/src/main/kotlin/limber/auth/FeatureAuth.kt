package limber.auth

import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID

private const val FEATURES_CLAIM_NAME = "features"

public object FeatureClaim

/**
 * Protects an endpoint to only members of the feature.
 */
public class FeatureAuth(private val featureGuid: UUID?) : Auth() {
  public constructor(featureGuid: () -> UUID?) : this(featureGuid())

  override fun authorize(context: RestContext): AuthResult {
    val principal = context.principal
      ?: return AuthResult.Unauthorized.noPrincipal()

    val features = getFeatureClaim(context, principal)
      ?: return AuthResult.Unauthorized.noClaim(FEATURES_CLAIM_NAME)

    if (featureGuid == null) return AuthResult.Failed

    if (featureGuid !in features) {
      return AuthResult.Failed
    }
    return AuthResult.Authorized
  }

  private fun getFeatureClaim(
    context: RestContext,
    principal: JWTPrincipal,
  ): Map<UUID, FeatureClaim>? =
    context.getClaim(principal, FEATURES_CLAIM_NAME)
}
