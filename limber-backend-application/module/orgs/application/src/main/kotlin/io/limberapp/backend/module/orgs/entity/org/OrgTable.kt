package io.limberapp.backend.module.orgs.entity.org

import com.piperframework.store.SqlTable

internal object OrgTable : SqlTable("orgs", "org") {

    val guid = uuid("guid")

    val name = text("name")
}
