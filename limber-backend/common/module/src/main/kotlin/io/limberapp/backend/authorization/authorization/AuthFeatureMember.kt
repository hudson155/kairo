package io.limberapp.backend.authorization.authorization

import io.limberapp.backend.authorization.Auth
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.permissions.featurePermissions.FeaturePermission
import io.limberapp.permissions.orgPermissions.OrgPermission
import java.util.*

class AuthFeatureMember private constructor(
    private val featureGuid: UUID,
    private val orgPermission: OrgPermission? = null,
    private val featurePermission: FeaturePermission? = null,
) : Auth() {
  constructor(featureGuid: UUID) : this(featureGuid, null, null)
  constructor(featureGuid: UUID, permission: OrgPermission) : this(featureGuid, permission, null)
  constructor(featureGuid: UUID, permission: FeaturePermission) : this(featureGuid, null, permission)

  override fun authorizeInternal(jwt: Jwt?): Boolean {
    jwt ?: return false
    val org = jwt.org ?: return false
    orgPermission?.let { if (it !in org.permissions) return false }
    val feature = org.features[featureGuid] ?: return false
    featurePermission?.let { if (it !in feature.permissions) return false }
    return true
  }
}
