package limber.service.feature

import com.google.inject.Inject
import limber.rep.feature.FeatureRep
import limber.store.feature.FeatureStore
import limber.util.guid.GuidGenerator
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class FeatureService @Inject constructor(
  private val guidGenerator: GuidGenerator,
  private val featureStore: FeatureStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun get(organizationGuid: UUID, hostnameGuid: UUID): FeatureRep? =
    featureStore.get(organizationGuid, hostnameGuid)

  fun getByOrganization(organizationGuid: UUID): List<FeatureRep> =
    featureStore.getByOrganization(organizationGuid)

  fun create(organizationGuid: UUID, creator: FeatureRep.Creator): FeatureRep {
    logger.info { "Creating feature: $creator." }
    val feature = FeatureRep(
      organizationGuid = organizationGuid,
      guid = guidGenerator.generate(),
      isDefault = false, // The store will change this to true if it's the only feature.
      type = creator.type,
      name = creator.name,
      rootPath = creator.rootPath,
    )
    return featureStore.create(feature)
  }

  fun update(organizationGuid: UUID, guid: UUID, updater: FeatureRep.Updater): FeatureRep {
    logger.info { "Updating feature: $updater." }
    if (updater.isDefault == true) {
      featureStore.setDefaultByOrganization(organizationGuid, guid)
    }
    return featureStore.update(organizationGuid, guid) { existing ->
      FeatureRep(
        organizationGuid = existing.organizationGuid,
        guid = existing.guid,
        isDefault = existing.isDefault,
        type = existing.type,
        name = updater.name ?: existing.name,
        rootPath = updater.rootPath ?: existing.rootPath,
      )
    }
  }

  fun delete(organizationGuid: UUID, guid: UUID): FeatureRep {
    logger.info { "Deleting feature." }
    return featureStore.delete(organizationGuid, guid)
  }
}
