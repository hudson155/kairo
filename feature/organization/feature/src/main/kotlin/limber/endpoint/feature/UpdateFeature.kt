package limber.endpoint.feature

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.exception.feature.FeatureDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.mapper.feature.FeatureMapper
import limber.model.feature.FeatureModel
import limber.service.feature.FeatureInterface
import limber.util.updater.update
import limber.api.feature.FeatureApi as Api
import limber.rep.feature.FeatureRep as Rep

public class UpdateFeature @Inject internal constructor(
  private val featureMapper: FeatureMapper,
  private val featureService: FeatureInterface,
) : RestEndpointHandler<Api.Update, Rep>(Api.Update::class) {
  override suspend fun handler(endpoint: Api.Update): Rep {
    auth(
      auth = OrganizationAuth(OrganizationPermission.Feature_Update) {
        featureService.get(endpoint.featureId)?.organizationId
      },
      onFail = { throw FeatureDoesNotExist() },
    )

    val update = getBody(endpoint)
    val feature = featureService.update(endpoint.featureId) { existing ->
      FeatureModel.Update(
        isDefault = update(existing.isDefault, update.isDefault),
        name = update(existing.name, update.name),
        iconName = update(existing.iconName, update.iconName),
        rootPath = update(existing.rootPath, update.rootPath),
      )
    }

    return featureMapper(feature)
  }
}
