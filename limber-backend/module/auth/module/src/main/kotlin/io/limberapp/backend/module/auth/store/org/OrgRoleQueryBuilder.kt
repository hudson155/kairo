package io.limberapp.backend.module.auth.store.org

import io.limberapp.backend.module.auth.model.org.OrgRoleFinder
import io.limberapp.common.store.QueryBuilder
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
      (EXISTS(SELECT 1
             FROM auth.org_role_membership
             WHERE org_role_guid = org_role.guid
               AND account_guid = :accountGuid) OR is_default IS TRUE)
      """.trimIndent()
    bindings["accountGuid"] = accountGuid
  }
}
