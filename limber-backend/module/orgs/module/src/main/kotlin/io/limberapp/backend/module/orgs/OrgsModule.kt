package io.limberapp.backend.module.orgs

import io.limberapp.backend.module.orgs.endpoint.org.DeleteOrg
import io.limberapp.backend.module.orgs.endpoint.org.GetByOwnerUserGuid
import io.limberapp.backend.module.orgs.endpoint.org.GetOrg
import io.limberapp.backend.module.orgs.endpoint.org.PatchOrg
import io.limberapp.backend.module.orgs.endpoint.org.PostOrg
import io.limberapp.backend.module.orgs.endpoint.org.feature.DeleteFeature
import io.limberapp.backend.module.orgs.endpoint.org.feature.PatchFeature
import io.limberapp.backend.module.orgs.endpoint.org.feature.PostFeature
import io.limberapp.backend.module.orgs.service.feature.FeatureServiceImpl
import io.limberapp.backend.module.orgs.service.org.FeatureService
import io.limberapp.backend.module.orgs.service.org.OrgService
import io.limberapp.backend.module.orgs.service.org.OrgServiceImpl
import io.limberapp.common.module.ApplicationModule

class OrgsModule : ApplicationModule() {
  override val endpoints = listOf(
    PostOrg::class.java,
    GetOrg::class.java,
    GetByOwnerUserGuid::class.java,
    PatchOrg::class.java,
    DeleteOrg::class.java,
    PostFeature::class.java,
    PatchFeature::class.java,
    DeleteFeature::class.java
  )

  override fun bindServices() {
    bind(OrgService::class.java).to(OrgServiceImpl::class.java).asEagerSingleton()
    bind(FeatureService::class.java).to(FeatureServiceImpl::class.java).asEagerSingleton()
  }
}
