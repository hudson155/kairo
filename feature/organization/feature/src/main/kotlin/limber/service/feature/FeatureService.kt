package limber.service.feature

import com.google.inject.Inject
import limber.feature.event.EventPublisher
import limber.feature.event.EventType
import limber.model.feature.FeatureModel
import limber.store.feature.FeatureStore
import limber.util.updater.Updater
import limber.util.updater.invoke

internal class FeatureService @Inject constructor(
  private val featureStore: FeatureStore,
  publisher: EventPublisher.Factory,
) {
  private val publisher: EventPublisher<FeatureModel> = publisher("feature")

  fun get(id: String): FeatureModel? =
    featureStore.get(id)

  fun listByOrganization(organizationId: String): List<FeatureModel> =
    featureStore.listByOrganization(organizationId)

  fun create(creator: FeatureModel.Creator): FeatureModel {
    val feature = featureStore.create(creator)
    publisher.publish(EventType.Created, feature)
    return feature
  }

  fun update(id: String, updater: Updater<FeatureModel.Update>): FeatureModel {
    val feature = featureStore.update(
      id = id,
      updater = updater { update ->
        if (update.isDefault) featureStore.setDefault(id)
      },
    )
    publisher.publish(EventType.Updated, feature)
    return feature
  }

  fun delete(id: String): FeatureModel {
    val feature = featureStore.delete(id)
    publisher.publish(EventType.Deleted, feature)
    return feature
  }
}
