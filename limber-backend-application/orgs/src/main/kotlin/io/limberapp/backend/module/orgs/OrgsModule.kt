package io.limberapp.backend.module.orgs

import io.limberapp.backend.module.orgs.endpoint.org.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrgById
import io.limberapp.backend.module.orgs.endpoint.org.GetOrgsByMemberId
import io.limberapp.backend.module.orgs.endpoint.org.UpdateOrg
import io.limberapp.backend.module.orgs.endpoint.org.membership.CreateMembership
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.orgs.service.org.OrgServiceImpl
import io.limberapp.backend.module.orgs.store.org.MongoOrgStore
import io.limberapp.backend.module.orgs.store.org.OrgStore
import io.limberapp.framework.module.Module

/**
 * The orgs module contains the basics of a client/tenant/organization. Be careful not to overload
 * this module with too much information about an org. Technically, almost everything could be
 * related back to the org and you could make an argument to put it in this module, but the
 * intention is to keep this module as slim as possible.
 */
class OrgsModule : Module() {

    override val endpoints = listOf(

        CreateMembership::class.java,

        CreateOrg::class.java,
        GetOrgById::class.java,
        GetOrgsByMemberId::class.java,
        UpdateOrg::class.java
    )

    override fun bindServices() {
        bind(OrgService::class, OrgServiceImpl::class)
    }

    override fun bindStores() {
        bind(OrgStore::class, MongoOrgStore::class)
    }
}
