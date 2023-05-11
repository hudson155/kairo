package limber.service.organization

import com.google.inject.Inject
import limber.feature.sql.update
import limber.model.organization.OrganizationModel
import limber.rep.organization.OrganizationRep
import limber.store.organization.OrganizationStore
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class OrganizationService @Inject constructor(
  private val organizationStore: OrganizationStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun get(guid: UUID): OrganizationModel? =
    organizationStore.get(guid)

  fun listAll(): List<OrganizationModel> =
    organizationStore.listAll()

  fun search(search: String): List<OrganizationModel> =
    organizationStore.search(search)

  fun create(creator: OrganizationModel.Creator): OrganizationModel {
    logger.info { "Creating organization: $creator." }
    return organizationStore.create(creator)
  }

  fun update(guid: UUID, updater: OrganizationRep.Updater): OrganizationModel {
    logger.info { "Updating organization: $updater." }
    return organizationStore.update(guid) { existing ->
      OrganizationModel.Updater(
        name = update(existing = existing.name, new = updater.name),
      )
    }
  }
}
