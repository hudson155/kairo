package limber.endpoint.feature

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.exception.feature.FeatureDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.mapper.feature.FeatureMapper
import limber.service.feature.FeatureService
import limber.api.feature.FeatureApi as Api
import limber.rep.feature.FeatureRep as Rep

public class DeleteFeature @Inject internal constructor(
  private val featureMapper: FeatureMapper,
  private val featureService: FeatureService,
) : RestEndpointHandler<Api.Delete, Rep>(Api.Delete::class) {
  override suspend fun handler(endpoint: Api.Delete): Rep {
    auth(
      auth = OrganizationAuth(OrganizationPermission.FeatureDelete) {
        featureService.get(endpoint.featureId)?.organizationGuid
      },
      onFail = { throw FeatureDoesNotExist() },
    )

    val feature = featureService.delete(endpoint.featureId)

    return featureMapper(feature)
  }
}
