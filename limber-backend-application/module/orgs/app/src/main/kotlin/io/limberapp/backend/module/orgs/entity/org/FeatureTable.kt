package io.limberapp.backend.module.orgs.entity.org

import com.piperframework.store.SqlTable

internal object FeatureTable : SqlTable("orgs", "feature") {

    val guid = uuid("guid")

    val orgGuid = uuid("org_guid").references(OrgTable.guid)
    const val orgGuidForeignKey = "feature_org_guid_fkey"

    val name = text("name")

    val path = text("path")
    const val orgPathUniqueConstraint = "feature_org_guid_path_key"

    val type = text("type")
}
