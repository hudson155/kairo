package limber.service.organization

import com.google.inject.ImplementedBy
import limber.model.organization.OrganizationModel
import limber.util.updater.Updater

@ImplementedBy(OrganizationService::class)
internal interface OrganizationInterface {
  suspend fun get(id: String): OrganizationModel?

  suspend fun listAll(): List<OrganizationModel>

  suspend fun search(search: String): List<OrganizationModel>

  suspend fun create(creator: OrganizationModel.Creator): OrganizationModel

  suspend fun update(id: String, updater: Updater<OrganizationModel.Update>): OrganizationModel

  suspend fun delete(id: String): OrganizationModel
}
