package io.limberapp.backend.module.auth.entity.tenant

import com.piperframework.store.SqlTable

object TenantTable : SqlTable("auth", "tenant") {

    val domain = text("domain")

    val orgGuid = uuid("org_guid")

    val auth0ClientId = text("auth0_client_id")
}
