package limber.endpoint.feature

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.service.feature.FeatureService
import limber.api.feature.FeatureApi as Api
import limber.rep.feature.FeatureRep as Rep

public class GetFeaturesByOrganization @Inject internal constructor(
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.GetByOrganization, List<Rep>>(Api.GetByOrganization::class) {
  override suspend fun handler(endpoint: Api.GetByOrganization): List<Rep> {
    auth(
      auth = OrganizationAuth(
        organizationGuid = endpoint.organizationGuid,
        permission = OrganizationPermission.FeatureRead,
      ),
      onFail = { return@handler emptyList() },
    )

    return featureService.getByOrganization(endpoint.organizationGuid)
  }
}
