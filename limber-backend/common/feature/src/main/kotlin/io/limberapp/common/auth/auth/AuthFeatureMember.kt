package io.limberapp.common.auth.auth

import io.limberapp.common.auth.Auth
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.permissions.featurePermissions.FeaturePermission
import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.permissions.orgPermissions.OrgPermission
import java.util.UUID

class AuthFeatureMember private constructor(
    private val featureGuid: UUID,
    private val orgPermission: OrgPermission? = null,
    private val featurePermission: FeaturePermission? = null,
) : Auth() {
  constructor(featureGuid: UUID) : this(
      featureGuid = featureGuid,
      orgPermission = null,
      featurePermission = null,
  )

  constructor(featureGuid: UUID, permission: OrgPermission) : this(
      featureGuid = featureGuid,
      orgPermission = permission,
      featurePermission = null,
  )

  constructor(featureGuid: UUID, permission: FeaturePermission) : this(
      featureGuid = featureGuid,
      orgPermission = null,
      featurePermission = permission,
  )

  override fun authorizeJwt(jwt: Jwt?): Boolean {
    jwt ?: return false
    val features = jwt.features ?: return false
    val feature = features[featureGuid] ?: return false
    if (orgPermission != null) {
      val org = jwt.org ?: return false
      if (orgPermission !in org.permissions) return false
    }
    if (featurePermission != null) {
      if (featurePermission !in feature.permissions) return false
    }
    return true
  }

  override fun authorizeOverride(jwt: Jwt): Boolean {
    if (LimberPermission.SUPERUSER in jwt.permissions) return true
    return super.authorizeOverride(jwt)
  }
}
