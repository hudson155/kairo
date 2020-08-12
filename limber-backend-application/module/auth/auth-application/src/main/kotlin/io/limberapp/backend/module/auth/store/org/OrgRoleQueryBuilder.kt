package io.limberapp.backend.module.auth.store.org

import com.piperframework.store.QueryBuilder
import io.limberapp.backend.module.auth.model.org.OrgRoleFinder
import java.util.*

internal class OrgRoleQueryBuilder : QueryBuilder(), OrgRoleFinder {
  override fun orgGuid(orgGuid: UUID) {
    conditions += "org_guid = :orgGuid"
    bindings["orgGuid"] = orgGuid
  }

  override fun orgRoleGuid(orgRoleGuid: UUID) {
    conditions += "guid = :orgRoleGuid"
    bindings["orgRoleGuid"] = orgRoleGuid
  }

  override fun accountGuid(accountGuid: UUID) {
    conditions +=
      """
      EXISTS(SELECT id
             FROM auth.org_role_membership
             WHERE org_role_guid = org_role.guid
               AND account_guid = :accountGuid)
      """.trimIndent()
    bindings["accountGuid"] = accountGuid
  }
}
