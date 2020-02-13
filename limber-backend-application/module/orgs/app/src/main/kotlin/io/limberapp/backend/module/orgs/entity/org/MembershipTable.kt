package io.limberapp.backend.module.orgs.entity.org

import com.piperframework.store.SqlTable

internal object MembershipTable : SqlTable("orgs", "membership") {

    val orgGuid = uuid("org_guid").references(OrgTable.guid)
    const val orgGuidForeignKey = "membership_org_guid_fkey"

    val accountGuid = uuid("account_guid")
}
