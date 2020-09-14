package io.limberapp.backend.module.auth.store.org

import com.piperframework.store.QueryBuilder
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipFinder
import java.util.*

internal class OrgRoleMembershipQueryBuilder : QueryBuilder(), OrgRoleMembershipFinder {
  override fun orgGuid(orgGuid: UUID) {
    conditions += "(SELECT org_guid FROM auth.org_role WHERE guid = org_role_guid) = :orgGuid"
    bindings["orgGuid"] = orgGuid
  }

  override fun orgRoleGuid(orgRoleGuid: UUID) {
    conditions += "org_role_guid = :orgRoleGuid"
    bindings["orgRoleGuid"] = orgRoleGuid
  }
}
