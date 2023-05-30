package limber.service.feature

import com.google.inject.Inject
import limber.model.feature.FeatureModel
import limber.store.feature.FeatureStore
import limber.util.updater.Updater
import limber.util.updater.invoke

internal class FeatureService @Inject constructor(
  private val featureStore: FeatureStore,
) {
  fun get(id: String): FeatureModel? =
    featureStore.get(id)

  fun listByOrganization(organizationId: String): List<FeatureModel> =
    featureStore.listByOrganization(organizationId)

  fun create(creator: FeatureModel.Creator): FeatureModel =
    featureStore.create(creator)

  fun update(id: String, updater: Updater<FeatureModel.Update>): FeatureModel =
    featureStore.update(
      id = id,
      updater = updater { update ->
        if (update.isDefault) featureStore.setDefault(id)
      },
    )

  fun delete(id: String): FeatureModel =
    featureStore.delete(id)
}
