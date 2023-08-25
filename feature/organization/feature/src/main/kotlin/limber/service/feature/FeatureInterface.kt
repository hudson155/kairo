package limber.service.feature

import com.google.inject.ImplementedBy
import limber.model.feature.FeatureModel
import limber.util.updater.Updater

@ImplementedBy(FeatureService::class)
internal interface FeatureInterface {
  fun get(id: String): FeatureModel?

  fun listByOrganization(organizationId: String): List<FeatureModel>

  fun create(creator: FeatureModel.Creator): FeatureModel

  fun update(id: String, updater: Updater<FeatureModel.Update>): FeatureModel

  fun delete(id: String): FeatureModel
}
