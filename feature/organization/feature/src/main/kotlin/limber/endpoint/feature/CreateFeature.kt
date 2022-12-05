package limber.endpoint.feature

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.service.feature.FeatureService
import limber.api.feature.FeatureApi as Api
import limber.rep.feature.FeatureRep as Rep

public class CreateFeature @Inject internal constructor(
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.Create, Rep>(Api.Create::class) {
  override suspend fun handler(endpoint: Api.Create): Rep {
    auth(
      auth = OrganizationAuth(
        organizationGuid = endpoint.organizationGuid,
        permission = OrganizationPermission.FeatureCreate,
      ),
      onFail = { throw OrganizationDoesNotExist() },
    )

    return featureService.create(endpoint.organizationGuid, getBody(endpoint))
  }
}
