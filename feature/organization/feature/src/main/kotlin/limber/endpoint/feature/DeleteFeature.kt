package limber.endpoint.feature

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.exception.feature.FeatureDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.mapper.feature.FeatureMapper
import limber.service.feature.FeatureInterface
import limber.api.feature.FeatureApi as Api
import limber.rep.feature.FeatureRep as Rep

public class DeleteFeature @Inject internal constructor(
  private val featureMapper: FeatureMapper,
  private val featureService: FeatureInterface,
) : RestEndpointHandler<Api.Delete, Rep>(Api.Delete::class) {
  override suspend fun handler(endpoint: Api.Delete): Rep {
    auth {
      val feature = featureService.get(endpoint.featureId) ?: throw FeatureDoesNotExist()
      return@auth OrganizationAuth(OrganizationPermission.Feature_Delete, feature.organizationId)
    }

    val feature = featureService.delete(endpoint.featureId)

    return featureMapper(feature)
  }
}
