package limber.endpoint.feature

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.mapper.feature.FeatureMapper
import limber.service.feature.FeatureInterface
import limber.api.feature.FeatureApi as Api
import limber.rep.feature.FeatureRep as Rep

public class ListFeaturesByOrganization @Inject internal constructor(
  private val featureMapper: FeatureMapper,
  private val featureService: FeatureInterface,
) : RestEndpointHandler<Api.ListByOrganization, List<Rep>>(Api.ListByOrganization::class) {
  override suspend fun handler(endpoint: Api.ListByOrganization): List<Rep> {
    auth { OrganizationAuth(OrganizationPermission.Feature_List, endpoint.organizationId) }

    val features = featureService.listByOrganization(endpoint.organizationId)

    return features.map { featureMapper(it) }
  }
}
