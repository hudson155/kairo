package limber.service

import com.google.inject.Inject
import limber.feature.guid.GuidGenerator
import limber.rep.FeatureRep
import limber.store.FeatureStore
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class FeatureService @Inject constructor(
  private val guidGenerator: GuidGenerator,
  private val featureStore: FeatureStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun create(organizationGuid: UUID, creator: FeatureRep.Creator): FeatureRep {
    logger.info { "Creating feature: $creator." }
    val feature = FeatureRep(
      organizationGuid = organizationGuid,
      guid = guidGenerator.generate(),
      isDefault = false, // The store will change this to true if it's the only Feature.
      type = creator.type,
      rootPath = creator.rootPath,
    )
    return featureStore.create(feature)
  }

  fun get(organizationGuid: UUID, hostnameGuid: UUID): FeatureRep? =
    featureStore.get(organizationGuid, hostnameGuid)

  fun getByOrganization(organizationGuid: UUID): List<FeatureRep> =
    featureStore.getByOrganization(organizationGuid)

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
        rootPath = updater.rootPath ?: existing.rootPath,
      )
    }
  }
}
