package io.limberapp.backend.module.auth.entity.tenant

import com.piperframework.store.SqlTable

internal object TenantDomainTable : SqlTable("auth", "tenant_domain") {

    val orgGuid = uuid("org_guid").references(TenantTable.orgGuid)
    const val orgGuidForeignKey = "tenant_domain_org_guid_fkey"

    val domain = text("domain")
    const val domainUniqueConstraint = "tenant_domain_domain_key"
}
