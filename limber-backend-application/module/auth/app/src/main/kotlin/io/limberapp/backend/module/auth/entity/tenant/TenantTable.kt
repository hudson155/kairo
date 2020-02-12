package io.limberapp.backend.module.auth.entity.tenant

import com.piperframework.store.SqlTable

object TenantTable : SqlTable("auth", "tenant") {

    val domain = text("domain")
    const val domainUniqueConstraint = "tenant_domain_key"

    val orgGuid = uuid("org_guid")
    const val orgGuidUniqueConstraint = "tenant_org_guid_key"

    val auth0ClientId = text("auth0_client_id")
    const val auth0ClientIdUniqueConstraint = "tenant_auth0_client_id_key"
}
