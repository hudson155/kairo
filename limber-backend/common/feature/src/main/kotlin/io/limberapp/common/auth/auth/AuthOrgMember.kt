package io.limberapp.common.auth.auth

import io.limberapp.common.auth.Auth
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.permissions.orgPermissions.OrgPermission
import java.util.UUID

class AuthOrgMember private constructor(
    private val orgGuid: UUID?,
    private val orgPermission: OrgPermission?,
    private val requireOrgOwnership: Boolean,
) : Auth() {
  constructor(orgGuid: UUID) : this(
      orgGuid = orgGuid,
      orgPermission = null,
      requireOrgOwnership = false,
  )

  constructor(orgGuid: UUID?, permission: OrgPermission) : this(
      orgGuid = orgGuid,
      orgPermission = permission,
      requireOrgOwnership = false,
  )

  constructor(orgGuid: UUID, isOwner: Boolean) : this(
      orgGuid = orgGuid,
      orgPermission = null,
      requireOrgOwnership = isOwner.also { require(it) },
  )

  override fun authorizeJwt(jwt: Jwt?): Boolean {
    jwt ?: return false
    val org = jwt.org ?: return false
    if (orgGuid != null) {
      if (org.guid != orgGuid) return false
    }
    if (orgPermission != null) {
      if (orgPermission !in org.permissions) return false
    }
    if (requireOrgOwnership) {
      if (!org.isOwner) return false
    }
    return true
  }

  override fun authorizeOverride(jwt: Jwt): Boolean {
    if (LimberPermission.SUPERUSER in jwt.permissions) return true
    return super.authorizeOverride(jwt)
  }
}
