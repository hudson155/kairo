package limber.service.organization

import com.google.inject.Inject
import limber.model.organization.OrganizationModel
import limber.store.organization.OrganizationStore
import limber.util.updater.Updater

internal class OrganizationService @Inject constructor(
  private val organizationStore: OrganizationStore,
) {
  fun get(id: String): OrganizationModel? =
    organizationStore.get(id)

  fun listAll(): List<OrganizationModel> =
    organizationStore.listAll()

  fun search(search: String): List<OrganizationModel> =
    organizationStore.search(search)

  fun create(creator: OrganizationModel.Creator): OrganizationModel =
    organizationStore.create(creator)

  fun update(id: String, updater: Updater<OrganizationModel.Update>): OrganizationModel =
    organizationStore.update(id, updater)
}
