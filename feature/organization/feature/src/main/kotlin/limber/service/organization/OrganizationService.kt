package limber.service.organization

import com.google.inject.Inject
import limber.model.organization.OrganizationModel
import limber.store.organization.OrganizationStore
import limber.util.updater.Updater
import java.util.UUID

internal class OrganizationService @Inject constructor(
  private val organizationStore: OrganizationStore,
) {
  fun get(guid: UUID): OrganizationModel? =
    organizationStore.getByGuid(guid)

  fun listAll(): List<OrganizationModel> =
    organizationStore.listAll()

  fun search(search: String): List<OrganizationModel> =
    organizationStore.search(search)

  fun create(creator: OrganizationModel.Creator): OrganizationModel =
    organizationStore.create(creator)

  fun update(guid: UUID, updater: Updater<OrganizationModel.Update>): OrganizationModel =
    organizationStore.update(guid, updater)
}
