package io.limberapp.module.orgs

import io.limberapp.endpoint.feature.DeleteFeature
import io.limberapp.endpoint.feature.GetFeature
import io.limberapp.endpoint.feature.PatchFeature
import io.limberapp.endpoint.feature.PostFeature
import io.limberapp.endpoint.org.DeleteOrg
import io.limberapp.endpoint.org.GetOrg
import io.limberapp.endpoint.org.GetOrgByOwnerUserGuid
import io.limberapp.endpoint.org.PatchOrg
import io.limberapp.endpoint.org.PostOrg
import io.limberapp.module.Feature
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.service.feature.FeatureService
import io.limberapp.service.feature.FeatureServiceImpl
import io.limberapp.service.org.OrgService
import io.limberapp.service.org.OrgServiceImpl
import kotlin.reflect.KClass

class OrgsFeature : Feature() {
  override val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> = listOf(
      PostOrg::class,
      GetOrg::class,
      GetOrgByOwnerUserGuid::class,
      PatchOrg::class,
      DeleteOrg::class,

      PostFeature::class,
      GetFeature::class,
      PatchFeature::class,
      DeleteFeature::class,
  )

  override fun bind() {
    bind(OrgService::class.java).to(OrgServiceImpl::class.java).asEagerSingleton()
    bind(FeatureService::class.java).to(FeatureServiceImpl::class.java).asEagerSingleton()
  }

  override fun cleanUp(): Unit = Unit
}
