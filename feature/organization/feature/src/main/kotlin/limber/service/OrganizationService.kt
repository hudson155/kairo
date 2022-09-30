package limber.service

import com.google.inject.Inject
import limber.feature.guid.GuidGenerator
import limber.rep.OrganizationRep
import limber.store.OrganizationStore
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class OrganizationService @Inject constructor(
  private val guidGenerator: GuidGenerator,
  private val organizationStore: OrganizationStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun create(creator: OrganizationRep.Creator): OrganizationRep {
    val organization = OrganizationRep(guid = guidGenerator.generate(), name = creator.name)
    logger.info { "Creating organization: $organization." }
    return organizationStore.create(organization)
  }

  fun get(organizationGuid: UUID): OrganizationRep? =
    organizationStore.get(organizationGuid)

  fun update(organizationGuid: UUID, updater: OrganizationRep.Updater): OrganizationRep {
    logger.info { "Updating organization: $updater." }
    return organizationStore.update(organizationGuid, updater)
  }
}
