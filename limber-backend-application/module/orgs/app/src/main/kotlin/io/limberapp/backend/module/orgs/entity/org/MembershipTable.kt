package io.limberapp.backend.module.orgs.entity.org

import com.piperframework.store.SqlTable

object MembershipTable : SqlTable("orgs", "membership") {
    val orgGuid = uuid("org_guid").references(OrgTable.guid)
    val accountGuid = uuid("account_guid")
}
