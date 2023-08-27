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

public class GetFeature @Inject internal constructor(
  private val featureMapper: FeatureMapper,
  private val featureService: FeatureInterface,
) : RestEndpointHandler<Api.Get, Rep?>(Api.Get::class) {
  override suspend fun handler(endpoint: Api.Get): Rep? {
    auth(
      auth = OrganizationAuth(
        permission = OrganizationPermission.Feature_Read,
        organizationId = featureService.get(endpoint.featureId)?.organizationId,
      ),
      onFail = { return@handler null },
    )

    val feature = featureService.get(endpoint.featureId)

    return feature?.let { featureMapper(it) }
  }
}
