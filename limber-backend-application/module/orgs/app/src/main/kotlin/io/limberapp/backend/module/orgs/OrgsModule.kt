package io.limberapp.backend.module.orgs

import com.piperframework.module.Module
import io.limberapp.backend.module.orgs.endpoint.org.PostOrg
import io.limberapp.backend.module.orgs.endpoint.org.DeleteOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetOrgsByMemberId
import io.limberapp.backend.module.orgs.endpoint.org.PatchOrg
import io.limberapp.backend.module.orgs.endpoint.org.feature.PostFeature
import io.limberapp.backend.module.orgs.endpoint.org.feature.DeleteFeature
import io.limberapp.backend.module.orgs.endpoint.org.feature.PatchFeature
import io.limberapp.backend.module.orgs.endpoint.org.membership.PostMembership
import io.limberapp.backend.module.orgs.endpoint.org.membership.DeleteMembership
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.FeatureServiceImpl
import io.limberapp.backend.module.orgs.service.org.MembershipService
import io.limberapp.backend.module.orgs.service.org.MembershipServiceImpl
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.orgs.service.org.OrgServiceImpl
import io.limberapp.backend.module.orgs.store.org.FeatureStore
import io.limberapp.backend.module.orgs.store.org.MembershipStore
import io.limberapp.backend.module.orgs.store.org.OrgStore
import io.limberapp.backend.module.orgs.store.org.SqlFeatureStore
import io.limberapp.backend.module.orgs.store.org.SqlMembershipStore
import io.limberapp.backend.module.orgs.store.org.SqlOrgMapper
import io.limberapp.backend.module.orgs.store.org.SqlOrgStore

class OrgsModule : Module() {

    override val endpoints = listOf(

        PostOrg::class.java,
        GetOrg::class.java,
        GetOrgsByMemberId::class.java,
        PatchOrg::class.java,
        DeleteOrg::class.java,

        PostFeature::class.java,
        PatchFeature::class.java,
        DeleteFeature::class.java,

        PostMembership::class.java,
        DeleteMembership::class.java
    )

    override fun bindServices() {
        bind(OrgService::class, OrgServiceImpl::class)
        bind(FeatureService::class, FeatureServiceImpl::class)
        bind(MembershipService::class, MembershipServiceImpl::class)
    }

    override fun bindStores() {
        bind(OrgStore::class, SqlOrgStore::class)
        bind(FeatureStore::class, SqlFeatureStore::class)
        bind(MembershipStore::class, SqlMembershipStore::class)
    }
}
