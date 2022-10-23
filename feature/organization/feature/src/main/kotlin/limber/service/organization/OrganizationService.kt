package limber.service.organization

import com.google.inject.Inject
import limber.rep.organization.OrganizationRep
import limber.store.organization.OrganizationStore
import limber.util.guid.GuidGenerator
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class OrganizationService @Inject constructor(
  private val guidGenerator: GuidGenerator,
  private val organizationStore: OrganizationStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun create(creator: OrganizationRep.Creator): OrganizationRep {
    logger.info { "Creating organization: $creator." }
    val organization = OrganizationRep(
      guid = guidGenerator.generate(),
      name = creator.name,
    )
    return organizationStore.create(organization)
  }

  fun get(guid: UUID): OrganizationRep? =
    organizationStore.get(guid)

  fun getByHostname(hostname: String): OrganizationRep? =
    organizationStore.getByHostname(hostname)

  fun update(guid: UUID, updater: OrganizationRep.Updater): OrganizationRep {
    logger.info { "Updating organization: $updater." }
    return organizationStore.update(guid) { existing ->
      OrganizationRep(
        guid = existing.guid,
        name = updater.name ?: existing.name,
      )
    }
  }
}
