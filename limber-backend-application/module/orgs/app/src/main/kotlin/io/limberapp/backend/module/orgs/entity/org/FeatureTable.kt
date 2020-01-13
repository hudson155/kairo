package io.limberapp.backend.module.orgs.entity.org

import com.piperframework.store.SqlTable

object FeatureTable : SqlTable("orgs", "feature") {
    val guid = uuid("guid")
    val orgGuid = uuid("org_guid").references(OrgTable.guid)
    val name = text("name")
    val path = text("path")
    val type = text("type")
}
