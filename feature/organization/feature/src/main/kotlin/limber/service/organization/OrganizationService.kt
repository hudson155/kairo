package limber.service.organization

import com.google.inject.Inject
import limber.feature.sql.update
import limber.mapper.organization.OrganizationMapper
import limber.model.organization.OrganizationModel
import limber.rep.organization.OrganizationRep
import limber.store.organization.OrganizationStore
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

internal class OrganizationService @Inject constructor(
  private val organizationMapper: OrganizationMapper,
  private val organizationStore: OrganizationStore,
) {
  private val logger: KLogger = KotlinLogging.logger {}

  fun get(guid: UUID): OrganizationModel? =
    organizationStore.get(guid)

  fun create(creator: OrganizationRep.Creator): OrganizationModel {
    logger.info { "Creating organization: $creator." }
    return organizationStore.create(organizationMapper(creator))
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
