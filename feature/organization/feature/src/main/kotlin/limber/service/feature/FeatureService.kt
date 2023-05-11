package limber.service.feature

import com.google.inject.Inject
import limber.feature.sql.update
import limber.model.feature.FeatureModel
import limber.rep.feature.FeatureRep
import limber.store.feature.FeatureStore
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

  fun update(guid: UUID, updater: FeatureRep.Updater): FeatureModel {
    logger.info { "Updating feature: $updater." }
    if (updater.isDefault == true) {
      featureStore.setDefault(guid)
    }
    return featureStore.update(guid) { existing ->
      FeatureModel.Updater(
        name = update(existing = existing.name, new = updater.name),
        iconName = update(existing = existing.iconName, new = updater.iconName),
        rootPath = update(existing = existing.rootPath, new = updater.rootPath),
      )
    }
  }

  fun delete(guid: UUID): FeatureModel {
    logger.info { "Deleting feature." }
    return featureStore.delete(guid)
  }
}
