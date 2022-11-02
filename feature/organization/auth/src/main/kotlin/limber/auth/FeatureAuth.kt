package limber.auth

import java.util.UUID

public object FeatureClaim

/**
 * Protects an endpoint to only members of the feature.
 */
public data class FeatureAuth(val featureGuid: UUID) : Auth() {
  override fun authorize(context: RestContext): Boolean {
    val features = context.getClaim<Map<UUID, FeatureClaim>>("features") ?: return false
    return featureGuid in features
  }
}
