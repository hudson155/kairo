package io.limberapp.backend.authorization.authorization

import io.limberapp.backend.authorization.Auth
import io.limberapp.common.auth.jwt.Jwt
import io.limberapp.common.permissions.orgPermissions.OrgPermission
import java.util.*

class AuthOrgMember private constructor(
    private val orgGuid: UUID?,
    private val orgPermission: OrgPermission?,
    private val isOwner: Boolean,
) : Auth() {
  constructor(orgGuid: UUID) : this(orgGuid, null, false)
  constructor(orgGuid: UUID?, permission: OrgPermission) : this(orgGuid, permission, false)
  constructor(orgGuid: UUID?, isOwner: Boolean) : this(orgGuid, null, isOwner.also { require(it) })

  override fun authorizeInternal(jwt: Jwt?): Boolean {
    jwt ?: return false
    val org = jwt.org ?: return false
    if (org.guid != orgGuid) return false
    orgPermission?.let { if (it !in org.permissions) return false }
    if (isOwner && !org.isOwner) return false
    return true
  }
}
