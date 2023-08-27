package limber.service.feature

import com.google.inject.ImplementedBy
import limber.model.feature.FeatureModel
import limber.util.updater.Updater

@ImplementedBy(FeatureService::class)
internal interface FeatureInterface {
  suspend fun get(id: String): FeatureModel?

  suspend fun listByOrganization(organizationId: String): List<FeatureModel>

  suspend fun create(creator: FeatureModel.Creator): FeatureModel

  suspend fun update(id: String, updater: Updater<FeatureModel.Update>): FeatureModel

  suspend fun delete(id: String): FeatureModel
}
