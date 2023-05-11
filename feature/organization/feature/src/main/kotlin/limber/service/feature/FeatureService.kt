package limber.service.feature

import com.google.inject.Inject
import limber.model.feature.FeatureModel
import limber.store.feature.FeatureStore
import limber.util.updater.Updater
import limber.util.updater.invoke
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class FeatureService @Inject constructor(
  private val featureStore: FeatureStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun get(guid: UUID): FeatureModel? =
    featureStore.get(guid)

  fun listByOrganization(organizationGuid: UUID): List<FeatureModel> =
    featureStore.listByOrganization(organizationGuid)

  fun create(creator: FeatureModel.Creator): FeatureModel {
    logger.info { "Creating feature: $creator." }
    return featureStore.create(creator)
  }

  fun update(guid: UUID, updater: Updater<FeatureModel.Update>): FeatureModel {
    logger.info { "Updating feature: $updater." }
    return featureStore.update(
      guid = guid,
      updater = updater { update ->
        if (update.isDefault) featureStore.setDefault(guid)
      },
    )
  }

  fun delete(guid: UUID): FeatureModel {
    logger.info { "Deleting feature." }
    return featureStore.delete(guid)
  }
}
