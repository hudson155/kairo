package limber.endpoint.feature

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.mapper.feature.FeatureMapper
import limber.service.feature.FeatureInterface
import limber.api.feature.FeatureApi as Api
import limber.rep.feature.FeatureRep as Rep

public class CreateFeature @Inject internal constructor(
  private val featureMapper: FeatureMapper,
  private val featureService: FeatureInterface,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    auth(
      auth = OrganizationAuth(OrganizationPermission.Feature_Create, endpoint.organizationId),
      onFail = { throw OrganizationDoesNotExist() },
    )

    val feature = featureService.create(
      creator = featureMapper(endpoint.organizationId, getBody(endpoint)),
    )

    return featureMapper(feature)
  }
}
