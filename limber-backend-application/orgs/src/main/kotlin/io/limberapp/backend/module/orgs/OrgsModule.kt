package io.limberapp.backend.module.orgs

import io.limberapp.backend.module.orgs.endpoint.formTemplate.CreateOrg
import io.limberapp.backend.module.orgs.endpoint.formTemplate.GetOrgById
import io.limberapp.backend.module.orgs.service.formTemplate.OrgService
import io.limberapp.backend.module.orgs.service.formTemplate.OrgServiceImpl
import io.limberapp.backend.module.orgs.store.formTemplate.MongoOrgStore
import io.limberapp.backend.module.orgs.store.formTemplate.OrgStore
import io.limberapp.framework.module.Module

/**
 * The orgs module contains the basics of a client/tenant/organization. Be careful not to overload
 * this module with too much information about an org. Technically, almost everything could be
 * related back to the org and you could make an argument to put it in this module, but the
 * intention is to keep this module as slim as possible.
 */
class OrgsModule : Module() {

    override val endpoints = listOf(
        CreateOrg::class.java,
        GetOrgById::class.java
    )

    override fun bindServices() {
        bind(OrgService::class, OrgServiceImpl::class)
    }

    override fun bindStores() {
        bind(OrgStore::class, MongoOrgStore::class)
    }
}
